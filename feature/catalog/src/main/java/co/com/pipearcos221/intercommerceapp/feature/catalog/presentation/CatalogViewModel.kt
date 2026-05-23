package co.com.pipearcos221.intercommerceapp.feature.catalog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    repository: ProductRepository
) : ViewModel() {

    val productsFlow: Flow<PagingData<Product>> = repository
        .getProducts()
        .cachedIn(viewModelScope)
}
