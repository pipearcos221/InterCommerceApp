package co.com.pipearcos221.intercommerceapp.core.data.repository

import android.util.Log
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
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ProductRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val productDao: ProductDao = mockk()
    private val apiService: ProductApiService = mockk()
    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setUp() {
        repository = ProductRepositoryImpl(productDao, apiService, testDispatcher)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when getProducts then return paging data flow`() = runTest(testDispatcher) {
        // Given
        every { productDao.getProducts() } returns mockk()

        // When
        val result = repository.getProducts()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `given an entity in database when getProductById then return domain product mapped correctly`() = runTest(testDispatcher) {
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
    fun `given successful api response when syncProducts then call dao to insert products`() = runTest(testDispatcher) {
        // Given
        val dtos = listOf(createFakeDto(id = 1, title = "Product 1"))
        val response = ProductResponseDto(products = dtos)
        coEvery { apiService.getProducts(any(), any()) } returns response
        coEvery { productDao.insertProducts(any()) } returns Unit

        // When
        val result = repository.syncProducts()

        // Then
        assert(result.isSuccess)
        coVerify(exactly = 1) { apiService.getProducts(any(), any()) }
        coVerify(exactly = 1) { productDao.insertProducts(any()) }
    }

    @Test
    fun `given api failure when syncProducts then return failure result`() = runTest(testDispatcher) {
        // Given
        val exception = IOException("No network")
        coEvery { apiService.getProducts(any(), any()) } throws exception

        // When
        val result = repository.syncProducts()

        // Then
        assert(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { apiService.getProducts(any(), any()) }
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
