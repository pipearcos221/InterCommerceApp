package co.com.pipearcos221.intercommerceapp.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "discount_percentage") val discountPercentage: Double,
    @ColumnInfo(name = "rating") val rating: Double,
    @ColumnInfo(name = "stock") val stock: Int,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    /**
     * IMPORTANTE: Room no puede guardar List<String> directamente.
     * Se requiere implementar un @TypeConverter en el Database o un archivo dedicado
     * para serializar/deserializar esta lista a un String (ej. usando JSON).
     */
    @ColumnInfo(name = "images") val images: List<String>
)
