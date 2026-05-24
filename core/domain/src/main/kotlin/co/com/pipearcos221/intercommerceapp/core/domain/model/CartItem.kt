package co.com.pipearcos221.intercommerceapp.core.domain.model

data class CartItem(
    val product: Product,
    val quantity: Int = 1
) {
    val originalPrice: Double get() = product.price
    val discountPercentage: Double get() = product.discountPercentage
    val priceWithDiscount: Double get() = product.priceWithDiscount
}
