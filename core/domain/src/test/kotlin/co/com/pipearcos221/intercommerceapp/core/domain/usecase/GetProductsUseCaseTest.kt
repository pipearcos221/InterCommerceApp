package co.com.pipearcos221.intercommerceapp.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProductsUseCaseTest {

    private val repository: ProductRepository = mockk()
    private lateinit var useCase: GetProductsUseCase

    @Before
    fun setUp() {
        useCase = GetProductsUseCase(repository)
    }

    @Test
    fun `given products in repository when use case is invoked then return products with calculated price`() = runTest {
        // Given
        val mockProducts = listOf(
            createMockProduct(id = 1, price = 100.0, discount = 10.0),
            createMockProduct(id = 2, price = 200.0, discount = 0.0)
        )
        val pagingData = PagingData.from(mockProducts)
        every { repository.getProducts() } returns flowOf(pagingData)

        // When
        val resultFlow = useCase()
        val snapshot = resultFlow.asSnapshot()

        // Then
        verify(exactly = 1) { repository.getProducts() }
        assertEquals(2, snapshot.size)
        
        // Check first product: 100.0 - 10% = 90.0
        assertEquals(90.0, snapshot[0].priceWithDiscount, 0.001)
        
        // Check second product: 200.0 - 0% = 200.0
        assertEquals(200.0, snapshot[1].priceWithDiscount, 0.001)
    }

    @Test
    fun `given empty products in repository when use case is invoked then return empty snapshot`() = runTest {
        // Given
        val pagingData = PagingData.from(emptyList<Product>())
        every { repository.getProducts() } returns flowOf(pagingData)

        // When
        val resultFlow = useCase()
        val snapshot = resultFlow.asSnapshot()

        // Then
        verify(exactly = 1) { repository.getProducts() }
        assertEquals(0, snapshot.size)
    }

    private fun createMockProduct(id: Int, price: Double, discount: Double): Product {
        return Product(
            id = id,
            title = "Test Product $id",
            description = "Desc",
            price = price,
            discountPercentage = discount,
            rating = 4.5,
            stock = 10,
            brand = "Brand",
            category = "Cat",
            thumbnail = "thumb",
            images = emptyList(),
            priceWithDiscount = 0.0 // Raw data from repo has default 0
        )
    }
}
