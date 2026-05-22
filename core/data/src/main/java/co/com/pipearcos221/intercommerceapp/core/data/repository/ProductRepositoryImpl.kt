package co.com.pipearcos221.intercommerceapp.core.data.repository

import co.com.pipearcos221.intercommerceapp.core.data.mapper.toDomain
import co.com.pipearcos221.intercommerceapp.core.data.mapper.toEntity
import co.com.pipearcos221.intercommerceapp.core.database.dao.ProductDao
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import co.com.pipearcos221.intercommerceapp.core.network.api.ProductApiService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementación del Repositorio de Productos.
 * Maneja el flujo de datos entre la API y la base de datos local.
 */
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val apiService: ProductApiService,
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        return productDao.getProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getProductById(productId: Int): Flow<Product?> {
        return productDao.getProductById(productId).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun syncProducts(): Result<Unit> {
        return try {
            val response = apiService.getProducts()
            val entities = response.products.map { it.toEntity() }
            productDao.insertProducts(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }
}
