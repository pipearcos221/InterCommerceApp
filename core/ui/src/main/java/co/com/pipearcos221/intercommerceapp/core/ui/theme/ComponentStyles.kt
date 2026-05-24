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
    const val ASPECT_RATIO_SQUARE = 1f
    const val PRODUCT_CARD_ASPECT_RATIO = 0.9f
    val errorIconSize = 80.dp
    val quickAddButtonSize = 32.dp
    val quickAddIconSize = 18.dp
    
    // Skeleton Specific Dimensions
    val skeletonTitleHeight = 16.dp
    val skeletonBrandHeight = 12.dp
    val skeletonPriceHeight = 14.dp
    val skeletonPriceWidth = 60.dp
    const val SKELETON_TITLE_WIDTH_RATIO = 0.7f
    const val SKELETON_BRAND_WIDTH_RATIO = 0.4f
    const val SKELETON_ITEM_COUNT = 10
    
    // Product Detail Specific
    val skeletonDetailCategoryWidth = 100.dp
    val skeletonDetailCategoryHeight = 16.dp
    val skeletonDetailTitleHeight = 32.dp
    val skeletonDetailPriceWidth = 120.dp
    val skeletonDetailPriceHeight = 24.dp
    val skeletonDetailLineHeight = 16.dp
    val immersiveBottomSpacing = 100.dp
    const val DETAIL_TITLE_WIDTH_RATIO = 0.7f
    const val DETAIL_DESCRIPTION_LINES = 5
    
    // Carousel Indicators
    val carouselIndicatorRowHeight = 50.dp
    val carouselIndicatorSize = 8.dp
    val carouselIndicatorPadding = 2.dp
    const val MIN_IMAGES_FOR_INDICATOR = 1
    
    // Animation & State Constants
    const val CART_ANIMATION_DELAY = 1500L
    const val BUTTON_LOADING_ALPHA = 0.7f
    val loaderSize = 24.dp
    val loaderStrokeWidth = 2.dp
    val successIconSize = 18.dp
    
    // Colors (Design Tokens)
    val successColor = Color(0xFF2E7D32)
    val discountBackgroundColor = Color(0xFFE8F5E9)
    val discountTextColor = Color(0xFF2E7D32)
    val overlayAlpha = 0.5f
    val overlayLightAlpha = 0.2f
    val shadowElevation = 8.dp
    
    // Animation Ratios & Thresholds
    const val PARALLAX_FACTOR = 0.5f
    const val PARALLAX_FADE_THRESHOLD = 1000f
    const val DESCRIPTION_LINE_HEIGHT_FACTOR = 1.2f
    const val INDEX_DISPLAY_OFFSET = 1
    const val ALPHA_FULL = 1f
    const val ALPHA_TRANSPARENT = 0f
    
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

    // Logic Constants
    const val EMPTY_COUNT = 0
    const val SINGLE_LINE = 1
}
