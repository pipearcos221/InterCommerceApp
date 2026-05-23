package co.com.pipearcos221.intercommerceapp.core.network.api

import co.com.pipearcos221.intercommerceapp.core.network.dto.ProductDto
import co.com.pipearcos221.intercommerceapp.core.network.dto.ProductResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductResponseDto

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductDto
}
