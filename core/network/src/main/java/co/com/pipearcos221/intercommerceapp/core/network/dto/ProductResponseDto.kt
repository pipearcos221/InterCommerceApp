package co.com.pipearcos221.intercommerceapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponseDto(
    @SerialName("products") val products: List<ProductDto>
)
