package co.com.pipearcos221.intercommerceapp.core.domain.usecase

import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculateCartTotalsUseCaseTest {

    private lateinit var calculateCartTotalsUseCase: CalculateCartTotalsUseCase

    @Before
    fun setUp() {
        calculateCartTotalsUseCase = CalculateCartTotalsUseCase()
    }

    @Test
    fun `given an empty cart when calculate total then return zero`() {
        // Given
        val items = emptyList<CartItem>()

        // When
        val result = calculateCartTotalsUseCase(items)

        // Then
        val expected = 0.0
        assertEquals(expected, result, 0.001)
    }

    @Test
    fun `given one product without discount when calculate total then return base price`() {
        // Given
        val product = createFakeProduct(price = 100.0, discount = 0.0)
        val items = listOf(CartItem(product, quantity = 1))

        // When
        val result = calculateCartTotalsUseCase(items)

        // Then
        val expected = 100.0
        assertEquals(expected, result, 0.001)
    }

    @Test
    fun `given one product with discount when calculate total then return discounted price`() {
        // Given
        val product = createFakeProduct(price = 100.0, discount = 10.0) // 10% de 100 = 10
        val items = listOf(CartItem(product, quantity = 2)) // (100 - 10) * 2 = 180

        // When
        val result = calculateCartTotalsUseCase(items)

        // Then
        val expected = 180.0
        assertEquals(expected, result, 0.001)
    }

    @Test
    fun `given multiple products with different quantities when calculate total then return sum of all`() {
        // Given
        val product1 = createFakeProduct(price = 50.0, discount = 0.0) // 50 * 2 = 100
        val product2 = createFakeProduct(price = 100.0, discount = 20.0) // 80 * 1 = 80
        val items = listOf(
            CartItem(product1, quantity = 2),
            CartItem(product2, quantity = 1)
        )

        // When
        val result = calculateCartTotalsUseCase(items)

        // Then
        val expected = 180.0
        assertEquals(expected, result, 0.001)
    }

    private fun createFakeProduct(price: Double, discount: Double): Product {
        return Product(
            id = 1,
            title = "Test Product",
            description = "Description",
            price = price,
            discountPercentage = discount,
            rating = 4.5,
            stock = 10,
            brand = "Brand",
            category = "Category",
            thumbnail = "url",
            images = emptyList()
        )
    }
}
