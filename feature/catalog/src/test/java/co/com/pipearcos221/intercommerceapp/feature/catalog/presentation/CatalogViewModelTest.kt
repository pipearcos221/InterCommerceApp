package co.com.pipearcos221.intercommerceapp.feature.catalog.presentation

import app.cash.turbine.test
import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.CartRepository
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.GetProductsUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val productRepository: ProductRepository = mockk()
    private val cartRepository: CartRepository = mockk()
    private val getProductsUseCase: GetProductsUseCase = mockk()
    private lateinit var viewModel: CatalogViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { cartRepository.getCartItems() } returns flowOf(emptyList())
        every { getProductsUseCase() } returns flowOf(mockk())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when initialized then should call repository functions`() = runTest {
        // WHEN
        viewModel = CatalogViewModel(productRepository, cartRepository, getProductsUseCase)

        // THEN
        assertNotNull(viewModel.productsFlow)
    }

    @Test
    fun `given items in cart when initialized then reflect cartCount`() = runTest {
        // GIVEN
        val product = createFakeProduct()
        val cartItems = listOf(CartItem(product, quantity = 3))
        every { cartRepository.getCartItems() } returns flowOf(cartItems)

        // WHEN
        viewModel = CatalogViewModel(productRepository, cartRepository, getProductsUseCase)

        // THEN
        assertEquals(3, viewModel.uiState.value.cartCount)
    }

    @Test
    fun `given a search query when changed then update uiState query and results`() = runTest {
        // GIVEN
        val products = listOf(createFakeProduct(title = "Nike Air"))
        coEvery { productRepository.searchProductsByQuery("Nike") } returns Result.success(products)
        
        // WHEN
        viewModel = CatalogViewModel(productRepository, cartRepository, getProductsUseCase)
        
        viewModel.uiState.test {
            assertEquals("", awaitItem().searchQuery)
            viewModel.onSearchQueryChanged("Nike")
            assertEquals("Nike", awaitItem().searchQuery)

            testDispatcher.scheduler.advanceTimeBy(500)
            
            val stateWithResults = awaitItem()
            assertTrue(stateWithResults.searchResults?.isNotEmpty() == true)
            assertEquals("Nike Air", stateWithResults.searchResults?.get(0)?.title)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createFakeProduct(title: String = "Product") = Product(
        id = (1..100).random(),
        title = title,
        description = "",
        price = 100.0,
        discountPercentage = 0.0,
        rating = 4.0,
        stock = 10,
        brand = "",
        category = "Cat",
        thumbnail = "",
        images = emptyList(),
        priceWithDiscount = 100.0
    )
}
