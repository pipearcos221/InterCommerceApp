package co.com.pipearcos221.intercommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceAppTheme
import co.com.pipearcos221.intercommerceapp.navigation.InterCommerceNavHost
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
    InterCommerceNavHost(navController = navController)
}
