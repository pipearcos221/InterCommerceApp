package co.com.pipearcos221.intercommerceapp.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val productId: Int,
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "category")
    val category: String,
    
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    
    @ColumnInfo(name = "original_price")
    val originalPrice: Double,
    
    @ColumnInfo(name = "discount_percentage")
    val discountPercentage: Double,
    
    @ColumnInfo(name = "price_with_discount")
    val priceWithDiscount: Double,
    
    @ColumnInfo(name = "quantity")
    val quantity: Int
)
