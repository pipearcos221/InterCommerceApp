package co.com.pipearcos221.intercommerceapp.feature.product_detail.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.com.pipearcos221.intercommerceapp.core.ui.component.shimmerEffect
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles

@Composable
fun ProductDetailSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Image Carousel Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(InterCommerceStyles.ASPECT_RATIO_SQUARE)
                .shimmerEffect()
        )
        
        Column(modifier = Modifier.padding(InterCommerceStyles.paddingLarge)) {
            // Category Placeholder
            Box(
                modifier = Modifier
                    .size(
                        width = InterCommerceStyles.skeletonDetailCategoryWidth,
                        height = InterCommerceStyles.skeletonDetailCategoryHeight
                    )
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(InterCommerceStyles.paddingMedium))
            
            // Title Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(InterCommerceStyles.DETAIL_TITLE_WIDTH_RATIO)
                    .height(InterCommerceStyles.skeletonDetailTitleHeight)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(InterCommerceStyles.paddingLarge))
            
            // Price Placeholder
            Box(
                modifier = Modifier
                    .size(
                        width = InterCommerceStyles.skeletonDetailPriceWidth,
                        height = InterCommerceStyles.skeletonDetailPriceHeight
                    )
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(InterCommerceStyles.paddingExtraLarge))
            
            // Description Lines Placeholder
            repeat(InterCommerceStyles.DETAIL_DESCRIPTION_LINES) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(InterCommerceStyles.skeletonDetailLineHeight)
                        .padding(vertical = InterCommerceStyles.paddingSmall)
                        .shimmerEffect()
                )
            }
        }
    }
}
