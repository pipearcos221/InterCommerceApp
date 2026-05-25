package co.com.pipearcos221.intercommerceapp.feature.product_detail.presentation

import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.ui.util.UiText

data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)
