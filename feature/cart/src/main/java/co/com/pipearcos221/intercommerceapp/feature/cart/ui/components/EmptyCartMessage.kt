package co.com.pipearcos221.intercommerceapp.feature.cart.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.cart.R

@Composable
fun EmptyCartMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.ShoppingBag,
            contentDescription = null,
            modifier = Modifier.size(InterCommerceStyles.emptyCartIconSize),
            tint = MaterialTheme.colorScheme.outline
        )
        Text(
            text = stringResource(R.string.cart_empty_message),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = InterCommerceStyles.paddingLarge)
        )
    }
}
