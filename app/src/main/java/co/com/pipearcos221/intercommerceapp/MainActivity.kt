package co.com.pipearcos221.intercommerceapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.CatalogRoute
import co.com.pipearcos221.intercommerceapp.core.ui.navigation.ProductDetailRoute
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceAppTheme
import co.com.pipearcos221.intercommerceapp.feature.catalog.ui.CatalogScreen
import co.com.pipearcos221.intercommerceapp.feature.product_detail.presentation.ProductDetailViewModel
import co.com.pipearcos221.intercommerceapp.feature.product_detail.ui.ProductDetailScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InterCommerceAppTheme {
                InterCommerceApp()
            }
        }
    }
}

@Composable
fun InterCommerceApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CatalogRoute
    ) {
        composable<CatalogRoute> {
            CatalogScreen(
                viewModel = hiltViewModel(),
                onProductClick = { productId ->
                    Log.d("Navigation", "Navigating to Product: $productId")
                    navController.navigate(ProductDetailRoute(productId = productId))
                }
            )
        }

        composable<ProductDetailRoute> {
            val viewModel: ProductDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            ProductDetailScreen(
                uiState = uiState,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
