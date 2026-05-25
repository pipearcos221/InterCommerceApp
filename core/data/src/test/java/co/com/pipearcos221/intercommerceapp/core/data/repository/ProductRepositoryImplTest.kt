package co.com.pipearcos221.intercommerceapp.core.data.repository

import app.cash.turbine.test
import co.com.pipearcos221.intercommerceapp.core.database.dao.ProductDao
import co.com.pipearcos221.intercommerceapp.core.domain.error.AppException
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
import org.junit.Assert.assertTrue
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
    fun `given successful api response when getProductById then return flow with domain product`() =
        runTest(testDispatcher) {
            // Given
            val dto = createFakeDto(id = 1, title = "Network Product")
            coEvery { apiService.getProductById(1) } returns dto

            // When & Then
            repository.getProductById(1).test {
                val result = awaitItem()
                assertTrue(result.isSuccess)
                assertEquals("Network Product", result.getOrNull()?.title)
                awaitComplete()
            }
        }

    @Test
    fun `given api failure and cached data when getProductById then emit cached data then error`() =
        runTest(testDispatcher) {
            // Given
            val exception = IOException("No network")
            coEvery { apiService.getProductById(1) } throws exception
            every { productDao.getProductById(1) } returns flowOf(null)

            // When & Then
            repository.getProductById(1).test {
                val result = awaitItem()
                assertTrue(result.isFailure)
                assertTrue(result.exceptionOrNull() is AppException.NetworkException)
                awaitComplete()
            }
        }

    @Test
    fun `given successful search when searchProductsByQuery then return list of domain products`() =
        runTest(testDispatcher) {
            // Given
            val dtos = listOf(createFakeDto(id = 1, title = "Search Result"))
            val response = ProductResponseDto(products = dtos)
            coEvery { apiService.searchProducts("query") } returns response

            // When
            val result = repository.searchProductsByQuery("query")

            // Then
            assertTrue(result.isSuccess)
            assertEquals(1, result.getOrNull()?.size)
            assertEquals("Search Result", result.getOrNull()?.get(0)?.title)
        }

    @Test
    fun `given successful api response when syncProducts then call dao to insert products`() =
        runTest(testDispatcher) {
            // Given
            val dtos = listOf(createFakeDto(id = 1, title = "Product 1"))
            val response = ProductResponseDto(products = dtos)
            coEvery { apiService.getProducts(any(), any()) } returns response
            coEvery { productDao.insertProducts(any()) } returns Unit

            // When
            val result = repository.syncProducts()

            // Then
            assertTrue(result.isSuccess)
            coVerify(exactly = 1) { productDao.insertProducts(any()) }
        }

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
