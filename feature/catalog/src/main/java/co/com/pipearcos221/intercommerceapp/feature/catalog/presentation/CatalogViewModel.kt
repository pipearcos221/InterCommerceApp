package co.com.pipearcos221.intercommerceapp.feature.catalog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<CatalogUiState> = combine(
        repository.getProducts(),
        _isLoading,
        _errorMessage
    ) { products, loading, error ->
        mapToUiState(products, loading, error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(SUBSCRIBE_TIMEOUT_MS),
        initialValue = CatalogUiState()
    )

    init {
        syncCatalog()
    }

    private fun syncCatalog() {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.syncProducts().fold(
                onSuccess = {
                    _errorMessage.value = null
                },
                onFailure = {
                    if (uiState.value.products.isEmpty()) {
                        _errorMessage.value = "Unable to load products. Please check your connection."
                    }
                }
            )
            
            _isLoading.value = false
        }
    }

    private fun mapToUiState(
        products: List<Product>,
        loading: Boolean,
        error: String?
    ): CatalogUiState {
        return CatalogUiState(
            products = products,
            isLoading = loading && products.isEmpty(),
            errorMessage = if (products.isEmpty()) error else null
        )
    }

    companion object {
        private const val SUBSCRIBE_TIMEOUT_MS = 5_000L
    }
}
