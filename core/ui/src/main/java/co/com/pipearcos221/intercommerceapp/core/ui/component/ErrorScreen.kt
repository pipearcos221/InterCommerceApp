package co.com.pipearcos221.intercommerceapp.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import co.com.pipearcos221.intercommerceapp.core.ui.R
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles

@Composable
fun ErrorScreen(
    message: String,
    icon: ImageVector,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.error_default_title),
    actionText: String = stringResource(R.string.error_action_retry),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(InterCommerceStyles.paddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(InterCommerceStyles.errorIconSize),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(InterCommerceStyles.paddingExtraLarge))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(InterCommerceStyles.paddingMedium))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(InterCommerceStyles.paddingLarge).padding(top = InterCommerceStyles.paddingMedium))
        Button(
            onClick = onAction,
            shape = InterCommerceStyles.buttonShape
        ) {
            Text(text = actionText)
        }
    }
}
