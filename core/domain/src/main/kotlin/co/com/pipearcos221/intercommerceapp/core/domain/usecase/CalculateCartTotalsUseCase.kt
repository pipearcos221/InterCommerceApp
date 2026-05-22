package co.com.pipearcos221.intercommerceapp.core.domain.usecase

import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import javax.inject.Inject

class CalculateCartTotalsUseCase @Inject constructor() {

    operator fun invoke(items: List<CartItem>): Double {
        return items.sumOf { item ->
            val discountFactor = UNITY_FACTOR - (item.product.discountPercentage / PERCENTAGE_BASE)
            (item.product.price * discountFactor) * item.quantity
        }
    }

    companion object {
        private const val UNITY_FACTOR = 1.0
        private const val PERCENTAGE_BASE = 100.0
    }
}
