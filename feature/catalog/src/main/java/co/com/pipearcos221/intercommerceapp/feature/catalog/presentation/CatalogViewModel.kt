package co.com.pipearcos221.intercommerceapp.feature.catalog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.CartRepository
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.GetProductsUseCase
import co.com.pipearcos221.intercommerceapp.core.domain.util.PriceCalculator.withCalculatedPrice
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.core.ui.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import co.com.pipearcos221.intercommerceapp.core.ui.R as Rcore

@OptIn(FlowPreview::class)
@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    val productsFlow: Flow<PagingData<Product>> = getProductsUseCase()
        .cachedIn(viewModelScope)

    init {
        observeSearchQuery()
        observeCartCount()
    }

    private fun observeSearchQuery() {
        _searchQuery
            .debounce(InterCommerceStyles.SEARCH_DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    _uiState.update { it.copy(searchResults = null, searchQuery = query) }
                } else {
                    executeSearch(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun executeSearch(query: String) {
        _uiState.update { it.copy(isLoading = true, searchQuery = query) }
        
        productRepository.searchProductsByQuery(query)
            .onSuccess { results ->
                val calculatedResults = results.map { it.withCalculatedPrice() }
                _uiState.update { it.copy(searchResults = calculatedResults, isLoading = false) }
            }
            .onFailure { exception ->
                val message = exception.localizedMessage?.let { UiText.DynamicString(it) }
                    ?: UiText.ResourceString(Rcore.string.error_unknown)
                _uiState.update { it.copy(errorMessage = message, isLoading = false) }
            }
    }

    private fun observeCartCount() {
        cartRepository.getCartItems()
            .map { items -> items.sumOf { it.quantity } }
            .onEach { count ->
                _uiState.update { it.copy(cartCount = count) }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        _uiState.update { 
            it.copy(
                searchQuery = newQuery,
                searchResults = if (newQuery.isBlank()) null else it.searchResults 
            ) 
        }
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(product)
        }
    }
}
