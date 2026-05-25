package co.com.pipearcos221.intercommerceapp.core.domain.repository

import androidx.paging.PagingData
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getProducts(): Flow<PagingData<Product>>

    fun getProductById(id: Int): Flow<Result<Product>>

    suspend fun syncProducts(): Result<Unit>
}
