package co.com.pipearcos221.intercommerceapp.core.ui.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = InterCommerceStyles.SHIMMER_TRANS_LABEL)
    
    val startOffsetX by transition.animateFloat(
        initialValue = InterCommerceStyles.SHIMMER_START_OFFSET_FACTOR * size.width.toFloat(),
        targetValue = InterCommerceStyles.SHIMMER_END_OFFSET_FACTOR * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(InterCommerceStyles.SHIMMER_DURATION_MS)
        ),
        label = InterCommerceStyles.SHIMMER_ANIM_LABEL
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                InterCommerceStyles.shimmerBaseColor,
                InterCommerceStyles.shimmerHighlightColor,
                InterCommerceStyles.shimmerBaseColor,
            ),
            start = Offset(startOffsetX, InterCommerceStyles.SHIMMER_Y_AXIS_ZERO),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}
