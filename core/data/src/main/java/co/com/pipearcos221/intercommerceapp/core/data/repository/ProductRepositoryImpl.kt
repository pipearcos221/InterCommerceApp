package co.com.pipearcos221.intercommerceapp.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.com.pipearcos221.intercommerceapp.core.data.di.qualifier.IoDispatcher
import co.com.pipearcos221.intercommerceapp.core.data.mapper.toDomain
import co.com.pipearcos221.intercommerceapp.core.data.mapper.toEntity
import co.com.pipearcos221.intercommerceapp.core.data.util.safeApiCall
import co.com.pipearcos221.intercommerceapp.core.database.dao.ProductDao
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import co.com.pipearcos221.intercommerceapp.core.network.api.ProductApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val apiService: ProductApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ProductRepository {

    override fun getProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            remoteMediator = ProductRemoteMediator(productDao, apiService),
            pagingSourceFactory = { productDao.getProducts() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getProductById(id: Int): Flow<Result<Product>> = flow {
        val networkResult = safeApiCall(ioDispatcher) {
            apiService.getProductById(id).toDomain()
        }
        if (networkResult.isSuccess) {
            emit(networkResult)
        } else {
            val cachedProduct = productDao.getProductById(id).firstOrNull()?.toDomain()
            if (cachedProduct != null) {
                emit(Result.success(cachedProduct))
                emit(Result.failure(networkResult.exceptionOrNull()!!))
            } else {
                emit(networkResult)
            }
        }
    }.flowOn(ioDispatcher)

    override suspend fun syncProducts(): Result<Unit> =
        safeApiCall(dispatcher = ioDispatcher) {
            val response = apiService.getProducts(limit = PAGE_SIZE, skip = INITIAL_SKIP_INDEX)
            productDao.insertProducts(response.products.map { it.toEntity() })
        }

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 5
        private const val INITIAL_SKIP_INDEX = 0
    }
}
