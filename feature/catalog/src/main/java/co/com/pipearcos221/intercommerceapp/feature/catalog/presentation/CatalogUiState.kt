package co.com.pipearcos221.intercommerceapp.feature.catalog.presentation

import co.com.pipearcos221.intercommerceapp.core.domain.model.Product

data class CatalogUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
