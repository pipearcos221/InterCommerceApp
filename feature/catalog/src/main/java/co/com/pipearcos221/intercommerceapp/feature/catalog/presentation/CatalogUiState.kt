package co.com.pipearcos221.intercommerceapp.feature.catalog.presentation

import co.com.pipearcos221.intercommerceapp.core.ui.util.UiText

data class CatalogUiState(
    val cartCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)
