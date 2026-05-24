package co.com.pipearcos221.intercommerceapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.CartRoute
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.CatalogRoute
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.CheckoutSuccessRoute
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.ProductDetailRoute
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.cart.presentation.CartViewModel
import co.com.pipearcos221.intercommerceapp.feature.cart.ui.CartScreen
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
            CatalogScreen(
                viewModel = hiltViewModel(),
                onProductClick = { productId ->
                    navController.navigate(ProductDetailRoute(productId = productId))
                },
                onCartClick = {
                    navController.navigate(CartRoute)
                }
            )
        }

        composable<ProductDetailRoute> {
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
                    onAddToCart = { product -> viewModel.addProductToCart(product) }
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
                    viewModel.checkout()
                    navController.navigate(CheckoutSuccessRoute) {
                        popUpTo(CatalogRoute) { inclusive = false }
                    }
                }
            )
        }

        composable<CheckoutSuccessRoute> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Order Placed Successfully! 🎉",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
