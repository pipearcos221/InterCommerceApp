package co.com.pipearcos221.intercommerceapp.core.network.dto

import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    @SerializedName("products") val products: List<ProductDto>
)
