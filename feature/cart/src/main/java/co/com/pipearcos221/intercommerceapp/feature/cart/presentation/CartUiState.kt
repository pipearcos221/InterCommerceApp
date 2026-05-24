package co.com.pipearcos221.intercommerceapp.feature.cart.presentation

import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val subtotalBeforeDiscounts: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val shipping: Double = 0.0,
    val totalToPay: Double = 0.0,
    val isLoading: Boolean = false,
    val isCheckoutSuccess: Boolean = false
)
