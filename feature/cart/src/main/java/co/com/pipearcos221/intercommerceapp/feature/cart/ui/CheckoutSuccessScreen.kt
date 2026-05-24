package co.com.pipearcos221.intercommerceapp.feature.cart.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.cart.R
import kotlinx.coroutines.delay

@Composable
fun CheckoutSuccessScreen(
    onFinished: () -> Unit
) {
    val scale = remember { Animatable(InterCommerceStyles.SCALE_INITIAL) }
    
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = InterCommerceStyles.SCALE_PEAK,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        scale.animateTo(InterCommerceStyles.SCALE_FINAL)

        delay(InterCommerceStyles.BUTTON_RESET_DELAY)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InterCommerceStyles.successColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(InterCommerceStyles.checkoutSuccessIconSize)
                    .scale(scale.value)
            )
            Spacer(modifier = Modifier.height(InterCommerceStyles.paddingLarge))
            Text(
                text = stringResource(R.string.cart_checkout_success_message),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
