package co.com.pipearcos221.intercommerceapp.feature.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.pipearcos221.intercommerceapp.core.domain.repository.CartRepository
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.CalculateCartTotalsUseCase
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.GetCartUseCase
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    getCartUseCase: GetCartUseCase,
    private val calculateCartTotalsUseCase: CalculateCartTotalsUseCase,
    private val cartRepository: CartRepository
) : ViewModel() {

    val uiState: StateFlow<CartUiState> = getCartUseCase()
        .map { items ->
            val totals = calculateCartTotalsUseCase(
                items = items, 
                shippingCost = SHIPPING_COST_FREE
            )
            
            CartUiState(
                items = items,
                subtotalBeforeDiscounts = totals.originalSubtotal,
                totalDiscount = totals.totalDiscount,
                shipping = totals.shipping,
                totalToPay = totals.total,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(InterCommerceStyles.FLOW_SUBSCRIPTION_TIMEOUT_MS),
            initialValue = CartUiState(isLoading = true)
        )

    fun updateQuantity(productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= MIN_QUANTITY_THRESHOLD) {
                cartRepository.removeFromCart(productId)
            } else {
                cartRepository.updateQuantity(productId, newQuantity)
            }
        }
    }

    fun removeItem(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }

    fun checkout() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    companion object {
        private const val MIN_QUANTITY_THRESHOLD = 0
        private const val SHIPPING_COST_FREE = 0.0
    }
}
