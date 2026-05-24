package co.com.pipearcos221.intercommerceapp.feature.cart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import coil.compose.AsyncImage
import co.com.pipearcos221.intercommerceapp.core.domain.model.CartItem
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.cart.R
import java.util.Locale

@Composable
fun CartItemRow(
    item: CartItem,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(InterCommerceStyles.paddingLarge)
    ) {
        AsyncImage(
            model = item.product.thumbnail,
            contentDescription = null,
            modifier = Modifier
                .size(InterCommerceStyles.cartImageSize)
                .clip(InterCommerceStyles.cardShape)
                .background(InterCommerceStyles.productCardBackground),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.product.category,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(InterCommerceStyles.paddingMedium))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.cart_qty_label, item.quantity),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { onUpdateQuantity(item.quantity.dec()) }) {
                    Text(
                        text = stringResource(R.string.cart_minus_label), 
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                IconButton(onClick = { onUpdateQuantity(item.quantity.inc()) }) {
                    Text(
                        text = stringResource(R.string.cart_plus_label), 
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Gray)
                }
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            if (item.discountPercentage > InterCommerceStyles.ZERO_PERCENTAGE) {
                Text(
                    text = stringResource(R.string.cart_price_format, item.originalPrice * item.quantity),
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.cart_price_format, item.priceWithDiscount * item.quantity),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = InterCommerceStyles.successColor
                )
            } else {
                Text(
                    text = stringResource(R.string.cart_price_format, item.originalPrice * item.quantity),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
