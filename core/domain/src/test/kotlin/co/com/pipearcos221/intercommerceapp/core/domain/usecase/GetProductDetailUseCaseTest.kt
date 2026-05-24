package co.com.pipearcos221.intercommerceapp.core.domain.usecase

import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetProductDetailUseCaseTest {

    private val repository: ProductRepository = mockk()
    private lateinit var useCase: GetProductDetailUseCase

    @Before
    fun setUp() {
        useCase = GetProductDetailUseCase(repository)
    }

    @Test
    fun `given a valid productId, when invoke detail, then return product with calculated price`() = runTest {
        // Given
        val productId = 1
        val mockProduct = createMockProduct(id = productId, price = 10.0, discount = 0.0)
        coEvery { repository.getProductById(productId) } returns Result.success(mockProduct)

        // When
        val result = useCase(productId)

        // Then
        assertTrue(result.isSuccess)
        val product = result.getOrNull()
        assertEquals(10.0, product?.priceWithDiscount ?: 0.0, 0.001)
        coVerify(exactly = 1) { repository.getProductById(productId) }
    }

    @Test
    fun `given a repository error, when use case is invoked, then return failure result`() = runTest {
        // Given
        val productId = 1
        val exception = Exception("Network Error")
        coEvery { repository.getProductById(productId) } returns Result.failure(exception)

        // When
        val result = useCase(productId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { repository.getProductById(productId) }
    }

    private fun createMockProduct(id: Int, price: Double, discount: Double): Product {
        return Product(
            id = id,
            title = "Test Product",
            description = "Desc",
            price = price,
            discountPercentage = discount,
            rating = 4.5,
            stock = 10,
            brand = "Brand",
            category = "Cat",
            thumbnail = "thumb",
            images = emptyList(),
            priceWithDiscount = 0.0
        )
    }
}
