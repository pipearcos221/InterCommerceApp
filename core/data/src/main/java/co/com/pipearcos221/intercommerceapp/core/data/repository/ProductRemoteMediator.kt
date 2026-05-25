package co.com.pipearcos221.intercommerceapp.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import co.com.pipearcos221.intercommerceapp.core.data.mapper.toEntity
import co.com.pipearcos221.intercommerceapp.core.data.util.mapToAppException
import co.com.pipearcos221.intercommerceapp.core.database.dao.ProductDao
import co.com.pipearcos221.intercommerceapp.core.database.entity.ProductEntity
import co.com.pipearcos221.intercommerceapp.core.network.api.ProductApiService
import kotlinx.coroutines.CancellationException

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator(
    private val productDao: ProductDao,
    private val apiService: ProductApiService
) : RemoteMediator<Int, ProductEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> STARTING_SKIP_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val count = productDao.getProductsCount()
                    if (count == EMPTY_DATABASE_COUNT) {
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }
                    count
                }
            }

            val response = apiService.getProducts(
                limit = state.config.pageSize,
                skip = loadKey
            )

            if (loadType == LoadType.REFRESH) {
                productDao.clearAllProducts()
            }

            val entities = response.products.map { it.toEntity() }
            productDao.insertProducts(entities)

            MediatorResult.Success(
                endOfPaginationReached = response.products.isEmpty()
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            MediatorResult.Error(mapToAppException(e))
        }
    }

    companion object {
        private const val STARTING_SKIP_INDEX = 0
        private const val EMPTY_DATABASE_COUNT = 0
    }
}
