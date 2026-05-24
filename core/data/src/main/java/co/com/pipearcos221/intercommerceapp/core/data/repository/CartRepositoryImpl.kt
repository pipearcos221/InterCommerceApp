package co.com.pipearcos221.intercommerceapp.core.data.repository

import co.com.pipearcos221.intercommerceapp.core.data.mapper.toCartEntity
import co.com.pipearcos221.intercommerceapp.core.data.mapper.toDomain
import co.com.pipearcos221.intercommerceapp.core.database.dao.CartDao
import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getCartItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToCart(product: Product, quantity: Int) {
        val entity = product.toCartEntity(quantity)
        val result = cartDao.insertOrIgnore(entity)

        if (result == ROOM_IGNORE_CONFLICT) {
            cartDao.incrementQuantity(product.id, quantity)
        }
    }

    override suspend fun updateQuantity(productId: Int, newQuantity: Int) {
        cartDao.updateQuantity(productId, newQuantity)
    }

    override suspend fun removeFromCart(productId: Int) {
        cartDao.deleteCartItem(productId)
    }

    companion object {
        private const val ROOM_IGNORE_CONFLICT = -1L
    }
}
