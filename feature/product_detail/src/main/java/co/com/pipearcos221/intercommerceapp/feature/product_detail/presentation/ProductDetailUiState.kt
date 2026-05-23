package co.com.pipearcos221.intercommerceapp.feature.product_detail.presentation

import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.ui.util.UiText

sealed interface ProductDetailUiState {
    data object Loading : ProductDetailUiState

    data class Success(val product: Product) : ProductDetailUiState

    data class Error(val message: UiText) : ProductDetailUiState
}
