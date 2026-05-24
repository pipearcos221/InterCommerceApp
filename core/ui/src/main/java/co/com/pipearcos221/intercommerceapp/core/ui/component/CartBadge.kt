package co.com.pipearcos221.intercommerceapp.core.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles

@Composable
fun CartBadge(
    count: Int,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onCartClick, modifier = modifier) {
        BadgedBox(
            badge = {
                if (count > InterCommerceStyles.EMPTY_COUNT) {
                    Badge {
                        Text(
                            text = count.toString(),
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.ShoppingBag,
                contentDescription = null
            )
        }
    }
}
