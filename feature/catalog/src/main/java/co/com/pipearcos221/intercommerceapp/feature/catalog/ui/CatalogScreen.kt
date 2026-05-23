package co.com.pipearcos221.intercommerceapp.feature.catalog.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.ui.component.ErrorScreen
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.catalog.R
import co.com.pipearcos221.intercommerceapp.feature.catalog.presentation.CatalogViewModel
import co.com.pipearcos221.intercommerceapp.feature.catalog.ui.components.ProductCard
import co.com.pipearcos221.intercommerceapp.feature.catalog.ui.components.ProductCardSkeleton
import co.com.pipearcos221.intercommerceapp.core.ui.R as Rcore

/**
 * Main Catalog Screen with Paging 3, Swipe-to-Refresh and Edge-to-Edge support.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagingItems = viewModel.productsFlow.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.catalog_app_bar_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        val refreshState = pagingItems.loadState.refresh
        val isRefreshing = refreshState is LoadState.Loading && 
                pagingItems.itemCount > InterCommerceStyles.EMPTY_COUNT

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { pagingItems.refresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            when (refreshState) {
                is LoadState.Loading -> {
                    if (pagingItems.itemCount == InterCommerceStyles.EMPTY_COUNT) {
                        LoadingGrid(innerPadding)
                    }
                }

                is LoadState.Error -> {
                    ErrorScreen(
                        message = refreshState.error.localizedMessage ?: stringResource(Rcore.string.error_unknown),
                        icon = Icons.Default.Warning,
                        onAction = { pagingItems.retry() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                else -> {
                    ProductGrid(
                        pagingItems = pagingItems,
                        innerPadding = innerPadding,
                        onProductClick = onProductClick,
                        onAddToCart = { _ ->
                            // TODO: Conectar con el ViewModel del Carrito en el siguiente Sprint
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductGrid(
    pagingItems: LazyPagingItems<Product>,
    innerPadding: PaddingValues,
    onProductClick: (Int) -> Unit,
    onAddToCart: (Product) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(InterCommerceStyles.adaptiveGridMinSize),
        contentPadding = PaddingValues(
            start = InterCommerceStyles.paddingLarge,
            end = InterCommerceStyles.paddingLarge,
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding() + InterCommerceStyles.paddingLarge
        ),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            CatalogHeader()
        }

        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems[index]?.id ?: index }
        ) { index ->
            pagingItems[index]?.let { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product.id) },
                    onAddToCart = onAddToCart
                )
            }
        }

        if (pagingItems.loadState.append is LoadState.Loading) {
            items(InterCommerceStyles.SKELETON_ITEM_COUNT) {
                ProductCardSkeleton()
            }
        }
    }
}

@Composable
private fun CatalogHeader() {
    Column(modifier = Modifier.padding(vertical = InterCommerceStyles.paddingLarge)) {
        Text(
            text = stringResource(R.string.catalog_greeting_user),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.catalog_section_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = InterCommerceStyles.paddingLarge)
        )
        Text(
            text = stringResource(R.string.catalog_section_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LoadingGrid(innerPadding: PaddingValues) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(InterCommerceStyles.adaptiveGridMinSize),
        contentPadding = PaddingValues(
            start = InterCommerceStyles.paddingLarge,
            end = InterCommerceStyles.paddingLarge,
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding() + InterCommerceStyles.paddingLarge
        ),
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            CatalogHeader()
        }
        items(InterCommerceStyles.SKELETON_ITEM_COUNT) {
            ProductCardSkeleton()
        }
    }
}
