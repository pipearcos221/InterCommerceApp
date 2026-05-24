package co.com.pipearcos221.intercommerceapp.feature.cart.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun PriceBreakdown(
    subtotal: Double,
    discount: Double,
    shipping: Double,
    total: Double,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = InterCommerceStyles.paddingLarge),
        verticalArrangement = Arrangement.spacedBy(InterCommerceStyles.paddingMedium)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(R.string.cart_subtotal_label), color = Color.Gray)
            Text(
                text = stringResource(R.string.cart_price_format, subtotal),
                fontWeight = FontWeight.Medium
            )
        }
        
        if (discount > InterCommerceStyles.ZERO_PERCENTAGE) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.cart_discount_label), color = Color.Gray)
                Text(
                    text = stringResource(R.string.cart_discount_price_format, discount),
                    color = InterCommerceStyles.successColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(R.string.cart_shipping_label), color = Color.Gray)
            Text(
                text = if (shipping > InterCommerceStyles.SHIPPING_COST_FREE) 
                    stringResource(R.string.cart_price_format, shipping) 
                else stringResource(R.string.cart_shipping_value),
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(InterCommerceStyles.paddingSmall))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(R.string.cart_total_label),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.cart_price_format, total),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
