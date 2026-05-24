package co.com.pipearcos221.intercommerceapp.feature.cart.presentation

import app.cash.turbine.test
import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.CartRepository
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.CalculateCartTotalsUseCase
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.CartTotals
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.GetCartUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getCartUseCase: GetCartUseCase = mockk()
    private val calculateCartTotalsUseCase: CalculateCartTotalsUseCase = mockk()
    private val cartRepository: CartRepository = mockk()
    private lateinit var viewModel: CartViewModel
    
    private val cartFlow = MutableStateFlow<List<CartItem>>(emptyList())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getCartUseCase() } returns cartFlow
        every { calculateCartTotalsUseCase(any(), any()) } returns CartTotals(0.0, 0.0, 0.0, 0.0)
        
        viewModel = CartViewModel(
            getCartUseCase,
            calculateCartTotalsUseCase,
            cartRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given items in cart when collect uiState then return correct breakdown`() = runTest {
        // Given
        val product = createFakeProduct(price = 100.0, discount = 10.0, priceWithDiscount = 90.0)
        val cartItems = listOf(CartItem(product, quantity = 2))
        val expectedTotals = CartTotals(
            originalSubtotal = 200.0,
            totalDiscount = 20.0,
            shipping = 0.0,
            total = 180.0
        )

        every { calculateCartTotalsUseCase(cartItems, 0.0) } returns expectedTotals

        // When
        cartFlow.value = cartItems

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(cartItems, state.items)
            assertEquals(expectedTotals.originalSubtotal, state.subtotalBeforeDiscounts, 0.001)
            assertEquals(expectedTotals.totalDiscount, state.totalDiscount, 0.001)
            assertEquals(expectedTotals.total, state.totalToPay, 0.001)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when updateQuantity with zero then call removeFromCart`() = runTest {
        // Given
        val productId = 1
        coEvery { cartRepository.removeFromCart(productId) } returns Unit

        // When
        viewModel.updateQuantity(productId, 0)

        // Then
        coVerify(exactly = 1) { cartRepository.removeFromCart(productId) }
    }

    @Test
    fun `when updateQuantity with positive value then call updateQuantity in repository`() = runTest {
        // Given
        val productId = 1
        val newQuantity = 5
        coEvery { cartRepository.updateQuantity(productId, newQuantity) } returns Unit

        // When
        viewModel.updateQuantity(productId, newQuantity)

        // Then
        coVerify(exactly = 1) { cartRepository.updateQuantity(productId, newQuantity) }
    }

    @Test
    fun `when removeItem then call repository removeFromCart`() = runTest {
        // Given
        val productId = 1
        coEvery { cartRepository.removeFromCart(productId) } returns Unit

        // When
        viewModel.removeItem(productId)

        // Then
        coVerify(exactly = 1) { cartRepository.removeFromCart(productId) }
    }

    @Test
    fun `when checkout then call repository clearCart`() = runTest {
        // Given
        coEvery { cartRepository.clearCart() } returns Unit

        // When
        viewModel.checkout()

        // Then
        coVerify(exactly = 1) { cartRepository.clearCart() }
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
