package co.com.pipearcos221.intercommerceapp.feature.catalog.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.com.pipearcos221.intercommerceapp.core.domain.error.AppException
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.ui.component.CartBadge
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
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagingItems = viewModel.productsFlow.collectAsLazyPagingItems()
    var isSearchActive by remember { mutableStateOf(false) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    
    val loadState = pagingItems.loadState
    LaunchedEffect(loadState.refresh, loadState.append) {
        val errorState = when {
            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
            else -> null
        }

        errorState?.let {
            if (pagingItems.itemCount > InterCommerceStyles.EMPTY_COUNT) {
                val message = if (it.error is AppException.NetworkException) {
                    context.getString(Rcore.string.error_no_internet_cached)
                } else {
                    it.error.localizedMessage ?: context.getString(Rcore.string.error_unknown)
                }
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    if (!isSearchActive) {
                        Text(
                            text = stringResource(R.string.catalog_app_bar_title),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        SearchTextField(
                            value = uiState.searchQuery,
                            onValueChange = viewModel::onSearchQueryChanged,
                            onCloseClick = { 
                                isSearchActive = false
                                viewModel.onSearchQueryChanged("")
                                // Nota: Al enviar "", el ViewModel limpia automáticamente searchResults
                            }
                        )
                    }
                },
                actions = {
                    if (!isSearchActive) {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.catalog_search_hint)
                            )
                        }
                    }
                    CartBadge(
                        count = uiState.cartCount,
                        onCartClick = onCartClick
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        val refreshState = pagingItems.loadState.refresh

        when {
            refreshState is LoadState.Loading && !isSearchActive -> LoadingGrid(innerPadding)

            refreshState is LoadState.Error
                    && pagingItems.itemCount == InterCommerceStyles.EMPTY_COUNT && !isSearchActive -> {
                ErrorScreen(
                    message = refreshState.error.localizedMessage
                        ?: stringResource(Rcore.string.error_unknown),
                    icon = Icons.Default.Warning,
                    onAction = { pagingItems.retry() },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                PullToRefreshBox(
                    isRefreshing = refreshState is LoadState.Loading,
                    onRefresh = { pagingItems.refresh() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    ProductGrid(
                        pagingItems = pagingItems,
                        searchResults = uiState.searchResults,
                        isSearchActive = isSearchActive,
                        contentPadding = innerPadding,
                        onProductClick = onProductClick,
                        onAddToCart = { product -> viewModel.addProductToCart(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onCloseClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Row(
        modifier = Modifier.fillMaxWidth().padding(end = InterCommerceStyles.paddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f).focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(R.string.catalog_search_hint),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        )
        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.catalog_search_clear_desc),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProductGrid(
    pagingItems: LazyPagingItems<Product>,
    searchResults: List<Product>?,
    isSearchActive: Boolean,
    contentPadding: PaddingValues,
    onProductClick: (Int) -> Unit,
    onAddToCart: (Product) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(InterCommerceStyles.adaptiveGridMinSize),
        contentPadding = PaddingValues(
            start = InterCommerceStyles.paddingLarge,
            end = InterCommerceStyles.paddingLarge,
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding() + InterCommerceStyles.paddingLarge
        ),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            CatalogHeader()
        }

        // Lógica condicional quirúrgica para cambiar entre búsqueda y catálogo normal
        if (isSearchActive && searchResults != null) {
            items(
                items = searchResults,
                key = { it.id }
            ) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product.id) },
                    onAddToCart = onAddToCart
                )
            }
        } else {
            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id },
                contentType = pagingItems.itemContentType { "Product" }
            ) { index ->
                val product = pagingItems[index]
                if (product != null) {
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
