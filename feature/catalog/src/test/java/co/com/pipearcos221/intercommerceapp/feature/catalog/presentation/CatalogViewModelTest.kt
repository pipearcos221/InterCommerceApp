package co.com.pipearcos221.intercommerceapp.feature.catalog.presentation

import androidx.paging.PagingData
import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.CartRepository
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.GetProductsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import app.cash.turbine.test

/**
 * Unit tests for [CatalogViewModel] following BDD best practices.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getProductsUseCase: GetProductsUseCase = mockk()
    private val cartRepository: CartRepository = mockk()
    private lateinit var viewModel: CatalogViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getProductsUseCase() } returns flowOf(PagingData.empty())
        every { cartRepository.getCartItems() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewModel is initialized, then it should call getProductsUseCase`() = runTest {
        // GIVEN
        every { getProductsUseCase() } returns flowOf(PagingData.from(emptyList()))

        // WHEN
        viewModel = CatalogViewModel(getProductsUseCase, cartRepository)

        // THEN
        assertNotNull(viewModel.productsFlow)
        verify(exactly = 1) { getProductsUseCase() }
    }

    @Test
    fun `given items in cart when collect uiState then reflect cartCount`() = runTest {
        // GIVEN
        val product = createFakeProduct()
        val cartItems = listOf(
            CartItem(product, quantity = 2),
            CartItem(product.copy(id = 2), quantity = 3)
        )
        every { cartRepository.getCartItems() } returns flowOf(cartItems)

        // WHEN
        viewModel = CatalogViewModel(getProductsUseCase, cartRepository)

        // THEN
        viewModel.uiState.test {
            val result = awaitItem()
            assertEquals(5, result.cartCount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createFakeProduct(): Product {
        return Product(
            id = 1,
            title = "Product",
            description = "",
            price = 100.0,
            discountPercentage = 0.0,
            rating = 4.5,
            stock = 10,
            brand = "Brand",
            category = "Cat",
            thumbnail = "thumb",
            images = emptyList(),
            priceWithDiscount = 100.0
        )
    }
}
