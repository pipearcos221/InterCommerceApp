package co.com.pipearcos221.intercommerceapp.core.network.api

import co.com.pipearcos221.intercommerceapp.core.network.dto.ProductResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz de Retrofit para interactuar con el catálogo de DummyJSON.
 */
interface ProductApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductResponseDto
}
