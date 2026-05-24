package co.com.pipearcos221.intercommerceapp.feature.cart.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.cart.R
import co.com.pipearcos221.intercommerceapp.feature.cart.presentation.CartUiState
import co.com.pipearcos221.intercommerceapp.feature.cart.ui.components.CartItemRow
import co.com.pipearcos221.intercommerceapp.feature.cart.ui.components.CheckoutBottomBar
import co.com.pipearcos221.intercommerceapp.feature.cart.ui.components.EmptyCartMessage
import co.com.pipearcos221.intercommerceapp.feature.cart.ui.components.PriceBreakdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    uiState: CartUiState,
    onBackClick: () -> Unit,
    onUpdateQuantity: (Int, Int) -> Unit,
    onRemoveItem: (Int) -> Unit,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.cart_app_bar_title), 
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            if (uiState.items.isNotEmpty()) {
                CheckoutBottomBar(
                    onCheckout = onCheckout
                )
            }
        }
    ) { innerPadding ->
        if (uiState.items.isEmpty() && !uiState.isLoading) {
            EmptyCartMessage(modifier = Modifier.padding(innerPadding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(InterCommerceStyles.paddingLarge)
            ) {
                items(uiState.items, key = { it.product.id }) { item ->
                    CartItemRow(
                        item = item,
                        onUpdateQuantity = { qty -> onUpdateQuantity(item.product.id, qty) },
                        onRemove = { onRemoveItem(item.product.id) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = InterCommerceStyles.paddingLarge),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }

                item {
                    PriceBreakdown(
                        subtotal = uiState.subtotalBeforeDiscounts,
                        discount = uiState.totalDiscount,
                        shipping = uiState.shipping,
                        total = uiState.totalToPay
                    )
                }
            }
        }
    }
}
