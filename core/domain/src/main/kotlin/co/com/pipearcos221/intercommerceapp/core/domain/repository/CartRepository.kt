package co.com.pipearcos221.intercommerceapp.core.domain.repository

import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getCartItems(): Flow<List<CartItem>>

    suspend fun addToCart(product: Product, quantity: Int = 1)

    suspend fun updateQuantity(productId: Int, newQuantity: Int)

    suspend fun removeFromCart(productId: Int)

    suspend fun clearCart()
}
