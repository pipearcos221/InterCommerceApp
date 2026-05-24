package co.com.pipearcos221.intercommerceapp.core.domain.usecase

import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculateCartTotalsUseCaseTest {

    private lateinit var useCase: CalculateCartTotalsUseCase

    @Before
    fun setUp() {
        useCase = CalculateCartTotalsUseCase()
    }

    @Test
    fun `given an empty cart when calculate total then return zero totals`() {
        // Given
        val items = emptyList<CartItem>()

        // When
        val result = useCase(items)

        // Then
        assertEquals(0.0, result.originalSubtotal, 0.001)
        assertEquals(0.0, result.totalDiscount, 0.001)
        assertEquals(0.0, result.total, 0.001)
    }

    @Test
    fun `given product with discount when calculate total then return correct breakdown`() {
        // Given
        // Price: 100.0, Discount: 10%, priceWithDiscount: 90.0
        val product = createFakeProduct(price = 100.0, discount = 10.0, priceWithDiscount = 90.0)
        val items = listOf(CartItem(product, quantity = 2)) // 200.0 total original, 180.0 total with discount

        // When
        val result = useCase(items)

        // Then
        assertEquals(200.0, result.originalSubtotal, 0.001)
        assertEquals(20.0, result.totalDiscount, 0.001)
        assertEquals(180.0, result.total, 0.001)
    }

    @Test
    fun `given multiple products when calculate total then return summed breakdown`() {
        // Given
        val product1 = createFakeProduct(id = 1, price = 50.0, discount = 0.0, priceWithDiscount = 50.0)
        val product2 = createFakeProduct(id = 2, price = 100.0, discount = 20.0, priceWithDiscount = 80.0)
        val items = listOf(
            CartItem(product1, quantity = 2), // 100.0 original, 0 discount
            CartItem(product2, quantity = 1)  // 100.0 original, 20 discount
        )

        // When
        val result = useCase(items)

        // Then
        assertEquals(200.0, result.originalSubtotal, 0.001)
        assertEquals(20.0, result.totalDiscount, 0.001)
        assertEquals(180.0, result.total, 0.001)
    }

    private fun createFakeProduct(
        id: Int = 1,
        price: Double,
        discount: Double,
        priceWithDiscount: Double
    ): Product {
        return Product(
            id = id,
            title = "Test Product",
            description = "",
            price = price,
            discountPercentage = discount,
            rating = 0.0,
            stock = 0,
            brand = "",
            category = "",
            thumbnail = "",
            images = emptyList(),
            priceWithDiscount = priceWithDiscount
        )
    }
}
