package co.com.pipearcos221.intercommerceapp.feature.catalog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import coil.compose.AsyncImage
import java.util.Locale

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current

    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(InterCommerceStyles.paddingMedium),
        shape = InterCommerceStyles.cardShape
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(InterCommerceStyles.PRODUCT_CARD_ASPECT_RATIO)
                    .background(InterCommerceStyles.productCardBackground)
            ) {
                AsyncImage(
                    model = product.thumbnail,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                if (product.discountPercentage > InterCommerceStyles.ZERO_PERCENTAGE) {
                    Surface(
                        color = InterCommerceStyles.discountBackgroundColor,
                        shape = CircleShape,
                        modifier = Modifier
                            .padding(InterCommerceStyles.paddingMedium)
                            .align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "-${product.discountPercentage.toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = InterCommerceStyles.discountTextColor,
                            modifier = Modifier.padding(
                                horizontal = InterCommerceStyles.paddingMedium, 
                                vertical = InterCommerceStyles.paddingSmall / 
                                        InterCommerceStyles.HALVED_FACTOR
                            ),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(InterCommerceStyles.paddingDefault)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = InterCommerceStyles.SINGLE_LINE,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = InterCommerceStyles.SINGLE_LINE,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(InterCommerceStyles.paddingMedium))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", product.priceWithDiscount)}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (product.discountPercentage > InterCommerceStyles.ZERO_PERCENTAGE) 
                                InterCommerceStyles.successColor else MaterialTheme.colorScheme.primary
                        )
                        if (product.discountPercentage > InterCommerceStyles.ZERO_PERCENTAGE) {
                            Text(
                                text = "$${String.format(Locale.US, "%.2f", product.price)}",
                                style = MaterialTheme.typography.bodySmall,
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onAddToCart(product)
                        },
                        modifier = Modifier
                            .size(InterCommerceStyles.quickAddButtonSize)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(InterCommerceStyles.quickAddIconSize)
                        )
                    }
                }
            }
        }
    }
}
