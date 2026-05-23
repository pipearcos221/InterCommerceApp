package co.com.pipearcos221.intercommerceapp.feature.catalog.presentation

import androidx.paging.PagingData
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: ProductRepository = mockk()
    private lateinit var viewModel: CatalogViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewModel is initialized, then it should call repository getProducts`() = runTest {
        // GIVEN
        every { repository.getProducts() } returns flowOf(PagingData.from(emptyList()))

        // WHEN
        viewModel = CatalogViewModel(repository)

        // THEN
        val flow = viewModel.productsFlow
        assertNotNull(flow)
        verify(exactly = 1) { repository.getProducts() }
    }
}
