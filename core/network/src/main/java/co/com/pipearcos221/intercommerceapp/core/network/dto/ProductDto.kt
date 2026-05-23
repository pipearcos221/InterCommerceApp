package co.com.pipearcos221.intercommerceapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("price") val price: Double = 0.0,
    @SerialName("discountPercentage") val discountPercentage: Double = 0.0,
    @SerialName("rating") val rating: Double = 0.0,
    @SerialName("stock") val stock: Int = 0,
    @SerialName("brand") val brand: String? = null,
    @SerialName("category") val category: String = "",
    @SerialName("thumbnail") val thumbnail: String = "",
    @SerialName("images") val images: List<String> = emptyList()
)
