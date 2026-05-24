package co.com.pipearcos221.intercommerceapp.core.domain.usecase

import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import javax.inject.Inject

data class CartTotals(
    val originalSubtotal: Double,
    val totalDiscount: Double,
    val shipping: Double,
    val total: Double
)

class CalculateCartTotalsUseCase @Inject constructor() {

    operator fun invoke(items: List<CartItem>, shippingCost: Double = 0.0): CartTotals {
        val originalSubtotal = items.sumOf { it.product.price * it.quantity }
        val totalDiscount = items.sumOf { (it.product.price - it.product.priceWithDiscount) * it.quantity }
        val total = (originalSubtotal - totalDiscount) + shippingCost
        
        return CartTotals(
            originalSubtotal = originalSubtotal,
            totalDiscount = totalDiscount,
            shipping = shippingCost,
            total = total
        )
    }
}
