package co.com.pipearcos221.intercommerceapp.core.network.api

import co.com.pipearcos221.intercommerceapp.core.network.dto.ProductResponseDto
import retrofit2.http.GET

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(): ProductResponseDto
}
