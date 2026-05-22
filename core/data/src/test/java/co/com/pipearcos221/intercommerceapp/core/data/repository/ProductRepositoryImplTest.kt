package co.com.pipearcos221.intercommerceapp.core.data.repository

import app.cash.turbine.test
import co.com.pipearcos221.intercommerceapp.core.database.dao.ProductDao
import co.com.pipearcos221.intercommerceapp.core.database.entity.ProductEntity
import co.com.pipearcos221.intercommerceapp.core.network.api.ProductApiService
import co.com.pipearcos221.intercommerceapp.core.network.dto.ProductDto
import co.com.pipearcos221.intercommerceapp.core.network.dto.ProductResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ProductRepositoryImplTest {

    private val productDao: ProductDao = mockk()
    private val apiService: ProductApiService = mockk()
    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setUp() {
        repository = ProductRepositoryImpl(productDao, apiService)
    }

    @Test
    fun `given entities in database when getProducts then return domain products mapped correctly`() = runTest {
        // Given
        val entities = listOf(createFakeEntity(id = 1, title = "Product 1"))
        every { productDao.getProducts() } returns flowOf(entities)

        // When & Then
        repository.getProducts().test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Product 1", result[0].title)
            awaitComplete()
        }
    }

    @Test
    fun `given an entity in database when getProductById then return domain product mapped correctly`() = runTest {
        // Given
        val entity = createFakeEntity(id = 1, title = "Product 1")
        every { productDao.getProductById(1) } returns flowOf(entity)

        // When & Then
        repository.getProductById(1).test {
            val result = awaitItem()
            assertEquals("Product 1", result?.title)
            awaitComplete()
        }
    }

    @Test
    fun `given successful api response when syncProducts then call dao to insert products`() = runTest {
        // Given
        val dtos = listOf(createFakeDto(id = 1, title = "Product 1"))
        val response = ProductResponseDto(products = dtos)
        coEvery { apiService.getProducts() } returns response
        coEvery { productDao.insertProducts(any()) } returns Unit

        // When
        repository.syncProducts()

        // Then
        coVerify(exactly = 1) { apiService.getProducts() }
        coVerify(exactly = 1) { productDao.insertProducts(any()) }
    }

    @Test
    fun `given api failure with IOException when syncProducts then handle exception silently and do not call dao`() = runTest {
        // Given
        coEvery { apiService.getProducts() } throws IOException("No network")

        // When
        repository.syncProducts()

        // Then
        coVerify(exactly = 1) { apiService.getProducts() }
        coVerify(exactly = 0) { productDao.insertProducts(any()) }
    }

    private fun createFakeEntity(id: Int, title: String) = ProductEntity(
        id = id,
        title = title,
        description = "Desc",
        price = 10.0,
        discountPercentage = 0.0,
        rating = 4.0,
        stock = 10,
        brand = "Brand",
        category = "Cat",
        thumbnail = "thumb",
        images = emptyList()
    )

    private fun createFakeDto(id: Int, title: String) = ProductDto(
        id = id,
        title = title,
        description = "Desc",
        price = 10.0,
        discountPercentage = 0.0,
        rating = 4.0,
        stock = 10,
        brand = "Brand",
        category = "Cat",
        thumbnail = "thumb",
        images = emptyList()
    )
}
