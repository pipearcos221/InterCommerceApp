package co.com.pipearcos221.intercommerceapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.CartRoute
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.CatalogRoute
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.CheckoutSuccessRoute
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.ProductDetailRoute
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.cart.presentation.CartViewModel
import co.com.pipearcos221.intercommerceapp.feature.cart.ui.CartScreen
import co.com.pipearcos221.intercommerceapp.feature.cart.ui.CheckoutSuccessScreen
import co.com.pipearcos221.intercommerceapp.feature.catalog.presentation.CatalogViewModel
import co.com.pipearcos221.intercommerceapp.feature.catalog.ui.CatalogScreen
import co.com.pipearcos221.intercommerceapp.feature.product_detail.presentation.ProductDetailViewModel
import co.com.pipearcos221.intercommerceapp.feature.product_detail.ui.ProductDetailActions
import co.com.pipearcos221.intercommerceapp.feature.product_detail.ui.ProductDetailScreen

@Composable
fun InterCommerceNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = CatalogRoute,
        modifier = modifier,
    ) {
        composable<CatalogRoute> {
            val viewModel: CatalogViewModel = hiltViewModel()
            CatalogScreen(
                viewModel = viewModel,
                onProductClick = { productId ->
                    navController.navigate(ProductDetailRoute(productId = productId))
                },
                onCartClick = {
                    navController.navigate(CartRoute)
                }
            )
        }

        composable<ProductDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ProductDetailRoute>()
            val viewModel: ProductDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val cartButtonState by viewModel.cartButtonState.collectAsState()
            val cartCount by viewModel.cartItemsCount.collectAsState()
            
            ProductDetailScreen(
                uiState = uiState,
                cartButtonState = cartButtonState,
                cartCount = cartCount,
                actions = ProductDetailActions(
                    onBackClick = { navController.popBackStack() },
                    onCartClick = { navController.navigate(CartRoute) },
                    onAddToCart = { product -> viewModel.addProductToCart(product) },
                    onRetry = { viewModel.loadProduct(route.productId) }
                )
            )
        }

        composable<CartRoute> {
            val viewModel: CartViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            CartScreen(
                uiState = uiState,
                onBackClick = { navController.popBackStack() },
                onUpdateQuantity = { id, qty -> viewModel.updateQuantity(id, qty) },
                onRemoveItem = { id -> viewModel.removeItem(id) },
                onCheckout = { 
                    navController.navigate(CheckoutSuccessRoute)
                }
            )
        }

        composable<CheckoutSuccessRoute> {
            val viewModel: CartViewModel = hiltViewModel()
            CheckoutSuccessScreen(
                onFinished = {
                    viewModel.checkout()
                    navController.navigate(CatalogRoute) {
                        popUpTo(CatalogRoute) { inclusive = true }
                    }
                }
            )
        }
    }
}
