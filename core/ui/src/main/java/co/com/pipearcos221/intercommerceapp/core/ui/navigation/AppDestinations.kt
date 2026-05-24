package co.com.pipearcos221.intercommerceapp.core.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object CatalogRoute

@Serializable
data class ProductDetailRoute(
    val productId: Int
)

@Serializable
object CartRoute

@Serializable
object CheckoutSuccessRoute
