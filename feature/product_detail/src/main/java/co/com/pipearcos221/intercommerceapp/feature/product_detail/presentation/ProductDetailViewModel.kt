package co.com.pipearcos221.intercommerceapp.feature.product_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.CartRepository
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.AddToCartUseCase
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.GetProductDetailUseCase
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.ProductDetailRoute
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.core.ui.util.UiText
import co.com.pipearcos221.intercommerceapp.core.ui.R as Rcore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class CartButtonState {
    Idle, Loading, Success
}

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<ProductDetailRoute>()

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _cartButtonState = MutableStateFlow(CartButtonState.Idle)
    val cartButtonState: StateFlow<CartButtonState> = _cartButtonState.asStateFlow()

    val cartItemsCount: StateFlow<Int> = cartRepository.getCartItems()
        .map { items -> items.sumOf { it.quantity } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(InterCommerceStyles.FLOW_SUBSCRIPTION_TIMEOUT_MS),
            initialValue = InterCommerceStyles.EMPTY_COUNT
        )

    init {
        loadProduct(route.productId)
    }

    private fun loadProduct(id: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            
            getProductDetailUseCase(id)
                .onSuccess { product ->
                    _uiState.value = ProductDetailUiState.Success(product)
                }
                .onFailure { exception ->
                    val message = exception.localizedMessage?.let { 
                        UiText.DynamicString(it) 
                    } ?: UiText.ResourceString(Rcore.string.error_unknown)
                    
                    _uiState.value = ProductDetailUiState.Error(message)
                }
        }
    }

    fun addProductToCart(product: Product) {
        if (_cartButtonState.value != CartButtonState.Idle) return

        viewModelScope.launch {
            _cartButtonState.value = CartButtonState.Loading

            addToCartUseCase(product)
            delay(InterCommerceStyles.CART_ANIMATION_DELAY)
            
            _cartButtonState.value = CartButtonState.Success
            delay(InterCommerceStyles.BUTTON_RESET_DELAY)
            _cartButtonState.value = CartButtonState.Idle
        }
    }
}
