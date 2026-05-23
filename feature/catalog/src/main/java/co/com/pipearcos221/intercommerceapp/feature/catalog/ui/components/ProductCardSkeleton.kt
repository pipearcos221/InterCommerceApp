package co.com.pipearcos221.intercommerceapp.feature.catalog.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import co.com.pipearcos221.intercommerceapp.core.ui.component.shimmerEffect
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles

@Composable
fun ProductCardSkeleton(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
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
                    .shimmerEffect()
            )
            Column(
                modifier = Modifier
                    .padding(InterCommerceStyles.paddingDefault)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(InterCommerceStyles.SKELETON_TITLE_WIDTH_RATIO)
                        .height(InterCommerceStyles.skeletonTitleHeight)
                        .clip(InterCommerceStyles.skeletonShape)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(InterCommerceStyles.paddingMedium))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(InterCommerceStyles.SKELETON_BRAND_WIDTH_RATIO)
                        .height(InterCommerceStyles.skeletonBrandHeight)
                        .clip(InterCommerceStyles.skeletonShape)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(InterCommerceStyles.paddingDefault))
                Box(
                    modifier = Modifier
                        .width(InterCommerceStyles.skeletonPriceWidth)
                        .height(InterCommerceStyles.skeletonPriceHeight)
                        .clip(InterCommerceStyles.skeletonShape)
                        .shimmerEffect()
                )
            }
        }
    }
}
