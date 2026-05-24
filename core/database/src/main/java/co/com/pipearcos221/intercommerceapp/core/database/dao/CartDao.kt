package co.com.pipearcos221.intercommerceapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.com.pipearcos221.intercommerceapp.core.database.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(cartItem: CartEntity): Long

    @Query("UPDATE cart_items SET quantity = quantity + :quantity WHERE product_id = :productId")
    suspend fun incrementQuantity(productId: Int, quantity: Int)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE product_id = :productId")
    suspend fun updateQuantity(productId: Int, quantity: Int)

    @Query("DELETE FROM cart_items WHERE product_id = :productId")
    suspend fun deleteCartItem(productId: Int)

    @Query("SELECT quantity FROM cart_items WHERE product_id = :productId")
    suspend fun getQuantityByProductId(productId: Int): Int?

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
