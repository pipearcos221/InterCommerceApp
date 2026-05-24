package co.com.pipearcos221.intercommerceapp.feature.catalog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.CartRepository
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    productRepository: ProductRepository,
    cartRepository: CartRepository
) : ViewModel() {

    val productsFlow: Flow<PagingData<Product>> = productRepository
        .getProducts()
        .cachedIn(viewModelScope)
    
    val cartItemsCount: StateFlow<Int> = cartRepository.getCartItems()
        .map { items -> 
            items.sumOf { it.quantity } 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(InterCommerceStyles.FLOW_SUBSCRIPTION_TIMEOUT_MS),
            initialValue = InterCommerceStyles.EMPTY_COUNT
        )
}
