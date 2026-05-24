package co.com.pipearcos221.intercommerceapp.core.domain.model

data class CartItem(
    val product: Product,
    val quantity: Int = 1
)
