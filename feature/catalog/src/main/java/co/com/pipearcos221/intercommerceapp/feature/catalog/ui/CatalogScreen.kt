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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.com.pipearcos221.intercommerceapp.core.ui.component.ErrorScreen
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.catalog.R
import co.com.pipearcos221.intercommerceapp.feature.catalog.presentation.CatalogViewModel
import co.com.pipearcos221.intercommerceapp.feature.catalog.ui.components.ProductCard
import co.com.pipearcos221.intercommerceapp.feature.catalog.ui.components.ProductCardSkeleton
import co.com.pipearcos221.intercommerceapp.core.ui.R as Rcore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

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
        when {
            state.errorMessage != null -> {
                ErrorScreen(
                    message = state.errorMessage ?: stringResource(Rcore.string.error_unknown),
                    icon = Icons.Default.Warning,
                    onAction = { viewModel.onRetry() },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
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

                    if (state.isLoading) {
                        items(InterCommerceStyles.SKELETON_ITEM_COUNT) {
                            ProductCardSkeleton()
                        }
                    } else {
                        items(state.products, key = { it.id }) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onProductClick(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
