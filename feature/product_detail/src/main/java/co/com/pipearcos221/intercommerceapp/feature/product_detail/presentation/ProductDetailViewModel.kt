package co.com.pipearcos221.intercommerceapp.feature.product_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.com.pipearcos221.intercommerceapp.core.domain.usecase.GetProductDetailUseCase
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.ProductDetailRoute
import co.com.pipearcos221.intercommerceapp.core.ui.util.UiText
import co.com.pipearcos221.intercommerceapp.core.ui.R as Rcore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<ProductDetailRoute>()

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProduct(route.productId)
    }

    private fun loadProduct(id: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            
            getProductDetailUseCase(id)
                .onSuccess { product ->
                    _uiState.value = ProductDetailUiState.Success(product)
                }
                .onFailure { exception ->
                    val message = exception.localizedMessage?.let { 
                        UiText.DynamicString(it) 
                    } ?: UiText.ResourceString(Rcore.string.error_unknown)
                    
                    _uiState.value = ProductDetailUiState.Error(message)
                }
        }
    }
}
