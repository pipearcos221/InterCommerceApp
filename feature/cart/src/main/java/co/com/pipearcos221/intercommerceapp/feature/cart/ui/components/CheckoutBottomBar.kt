package co.com.pipearcos221.intercommerceapp.feature.cart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.cart.R

@Composable
fun CheckoutBottomBar(
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
            .padding(InterCommerceStyles.paddingLarge)
    ) {
        Button(
            onClick = onCheckout,
            modifier = Modifier
                .fillMaxWidth()
                .height(InterCommerceStyles.checkoutButtonHeight),
            shape = InterCommerceStyles.buttonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.cart_checkout_button),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
