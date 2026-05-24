package co.com.pipearcos221.intercommerceapp.core.domain.usecase

import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.CartRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCartUseCaseTest {

    private lateinit var repository: CartRepository
    private lateinit var useCase: GetCartUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCartUseCase(repository)
    }

    @Test
    fun `given repository returns items when invoke then apply price calculation`() = runTest {
        // Given
        val product = Product(
            id = 1,
            title = "Product",
            description = "",
            price = 100.0,
            discountPercentage = 10.0,
            rating = 0.0,
            stock = 0,
            brand = "",
            category = "",
            thumbnail = "",
            images = emptyList(),
            priceWithDiscount = 0.0 // Initially 0
        )
        val cartItems = listOf(CartItem(product, quantity = 1))
        every { repository.getCartItems() } returns flowOf(cartItems)

        // When
        val result = useCase().first()

        // Then
        verify(exactly = 1) { repository.getCartItems() }
        assertEquals(90.0, result.first().product.priceWithDiscount, 0.001)
    }
}
