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
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementación del Repositorio de Productos.
 * Sigue el patrón Offline-First (SSOT): la UI solo observa cambios de la DB local.
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

    override suspend fun syncProducts() {
        try {
            val response = apiService.getProducts()
            val entities = response.products.map { it.toEntity() }
            productDao.insertProducts(entities)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
