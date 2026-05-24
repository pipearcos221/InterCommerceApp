package co.com.pipearcos221.intercommerceapp.core.domain.util

import co.com.pipearcos221.intercommerceapp.core.domain.model.Product

object PriceCalculator {
    private const val PERCENTAGE_DIVISOR = 100.0
    private const val BASE_UNIT = 1.0

    fun calculatePriceWithDiscount(price: Double, discountPercentage: Double): Double {
        if (discountPercentage <= 0) return price
        return price * (BASE_UNIT - (discountPercentage / PERCENTAGE_DIVISOR))
    }

    fun Product.withCalculatedPrice(): Product {
        return this.copy(
            priceWithDiscount = calculatePriceWithDiscount(price, discountPercentage)
        )
    }
}
