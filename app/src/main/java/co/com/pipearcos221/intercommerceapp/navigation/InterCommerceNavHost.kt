package co.com.pipearcos221.intercommerceapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.CatalogRoute
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.ProductDetailRoute
import co.com.pipearcos221.intercommerceapp.feature.catalog.ui.CatalogScreen
import co.com.pipearcos221.intercommerceapp.feature.product_detail.presentation.ProductDetailViewModel
import co.com.pipearcos221.intercommerceapp.feature.product_detail.ui.ProductDetailScreen

@Composable
fun InterCommerceNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = CatalogRoute,
        modifier = modifier
    ) {
        composable<CatalogRoute> {
            CatalogScreen(
                viewModel = hiltViewModel(),
                onProductClick = { productId ->
                    navController.navigate(ProductDetailRoute(productId = productId))
                }
            )
        }

        composable<ProductDetailRoute> {
            val viewModel: ProductDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val cartButtonState by viewModel.cartButtonState.collectAsState()
            
            ProductDetailScreen(
                uiState = uiState,
                cartButtonState = cartButtonState,
                onBackClick = { navController.popBackStack() },
                onAddToCart = { product -> viewModel.addProductToCart(product) }
            )
        }
    }
}
