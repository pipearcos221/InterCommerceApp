package co.com.pipearcos221.intercommerceapp.feature.product_detail.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.activity.compose.BackHandler
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.product_detail.R
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ProductImagePreviewDialog(
    images: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        BackHandler(onBack = onDismiss)

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            val pagerState = rememberPagerState(
                initialPage = initialIndex,
                pageCount = { images.size }
            )
            
            Box(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    pageSpacing = InterCommerceStyles.paddingLarge
                ) { page ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(images[page])
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                // Top Navigation & Indicator Overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(InterCommerceStyles.paddingLarge)
                ) {
                    // Close Button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.product_detail_back_desc),
                            tint = Color.White
                        )
                    }

                    //  Counter
                    if (images.size > InterCommerceStyles.MIN_IMAGES_FOR_INDICATOR) {
                        Surface(
                            color = Color.White.copy(alpha = InterCommerceStyles.overlayLightAlpha),
                            shape = CircleShape,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clip(CircleShape)
                        ) {
                            val currentDisplayIndex = pagerState.currentPage + 
                                    InterCommerceStyles.INDEX_DISPLAY_OFFSET
                            
                            Text(
                                text = "$currentDisplayIndex / ${images.size}",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(
                                    horizontal = InterCommerceStyles.paddingLarge, 
                                    vertical = InterCommerceStyles.paddingMedium
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
