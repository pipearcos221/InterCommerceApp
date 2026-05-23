package co.com.pipearcos221.intercommerceapp.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Design system tokens para InterCommerce App.
 * Centraliza dimensiones y formas para evitar "Magic Numbers".
 */
object InterCommerceStyles {
    // Shapes
    val cardShape = RoundedCornerShape(16.dp)
    val buttonShape = RoundedCornerShape(12.dp)
    val skeletonShape = RoundedCornerShape(4.dp)

    // Spacing
    val paddingSmall = 4.dp
    val paddingMedium = 8.dp
    val paddingDefault = 12.dp
    val paddingLarge = 16.dp
    val paddingExtraLarge = 24.dp
    
    // Grid & Layout
    val adaptiveGridMinSize = 160.dp
    const val PRODUCT_CARD_ASPECT_RATIO = 0.9f
    val errorIconSize = 80.dp
    
    // Skeleton Specific Dimensions
    val skeletonTitleHeight = 16.dp
    val skeletonBrandHeight = 12.dp
    val skeletonPriceHeight = 14.dp
    val skeletonPriceWidth = 60.dp
    const val SKELETON_TITLE_WIDTH_RATIO = 0.7f
    const val SKELETON_BRAND_WIDTH_RATIO = 0.4f
    const val SKELETON_ITEM_COUNT = 10
    
    // Shimmer Animation Constants
    const val SHIMMER_DURATION_MS = 1200
    const val SHIMMER_START_OFFSET_FACTOR = -2f
    const val SHIMMER_END_OFFSET_FACTOR = 2f
    const val SHIMMER_Y_AXIS_ZERO = 0f
    
    // Shimmer Colors
    val shimmerBaseColor = Color(0xFFEBEBF4)
    val shimmerHighlightColor = Color(0xFFF4F4F4)
    val productCardBackground = Color(0xFFF5F5F5)
    
    // Animation Labels
    const val SHIMMER_ANIM_LABEL = "shimmer_animation"
    const val SHIMMER_TRANS_LABEL = "shimmer_transition"
}
