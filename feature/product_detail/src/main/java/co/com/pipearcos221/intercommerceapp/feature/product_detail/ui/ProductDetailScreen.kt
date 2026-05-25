package co.com.pipearcos221.intercommerceapp.feature.product_detail.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import co.com.pipearcos221.intercommerceapp.core.domain.model.Product
import co.com.pipearcos221.intercommerceapp.core.ui.component.CartBadge
import co.com.pipearcos221.intercommerceapp.core.ui.component.ErrorScreen
import co.com.pipearcos221.intercommerceapp.core.ui.theme.InterCommerceStyles
import co.com.pipearcos221.intercommerceapp.feature.product_detail.R
import co.com.pipearcos221.intercommerceapp.feature.product_detail.presentation.CartButtonState
import co.com.pipearcos221.intercommerceapp.feature.product_detail.presentation.ProductDetailUiState
import co.com.pipearcos221.intercommerceapp.feature.product_detail.ui.components.ProductDetailSkeleton
import co.com.pipearcos221.intercommerceapp.feature.product_detail.ui.components.ProductImageCarousel
import co.com.pipearcos221.intercommerceapp.feature.product_detail.ui.components.ProductImagePreviewDialog
import java.util.Locale

data class ProductDetailActions(
    val onBackClick: () -> Unit,
    val onCartClick: () -> Unit,
    val onAddToCart: (Product) -> Unit,
    val onRetry: () -> Unit
)

@Composable
fun ProductDetailScreen(
    uiState: ProductDetailUiState,
    cartButtonState: CartButtonState,
    cartCount: Int,
    actions: ProductDetailActions,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        val errorMessage = uiState.errorMessage
        if (errorMessage != null && uiState.product != null) {
            snackbarHostState.showSnackbar(
                message = errorMessage.asString(context)
            )
        }
    }

    LaunchedEffect(cartButtonState) {
        if (cartButtonState == CartButtonState.Success) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading && uiState.product == null -> {
                ProductDetailSkeleton()
            }
            uiState.errorMessage != null && uiState.product == null -> {
                ErrorScreen(
                    message = uiState.errorMessage.asString(),
                    icon = Icons.Default.Warning,
                    onAction = actions.onRetry
                )
            }
            uiState.product != null -> {
                ProductDetailContent(
                    product = uiState.product,
                    cartButtonState = cartButtonState,
                    cartCount = cartCount,
                    actions = actions,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailContent(
    product: Product,
    cartButtonState: CartButtonState,
    cartCount: Int,
    actions: ProductDetailActions,
    snackbarHostState: SnackbarHostState
) {
    val scrollState = rememberScrollState()
    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AddToBagButton(
                state = cartButtonState,
                onClick = { actions.onAddToCart(product) }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                ProductImageCarousel(
                    images = product.images,
                    productId = product.id,
                    onImageClick = { index -> selectedImageIndex = index },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(InterCommerceStyles.ASPECT_RATIO_SQUARE)
                        .graphicsLayer {
                            translationY = scrollState.value * InterCommerceStyles.PARALLAX_FACTOR
                            alpha = (InterCommerceStyles.ALPHA_FULL - (scrollState.value.toFloat() / 
                                    InterCommerceStyles.PARALLAX_FADE_THRESHOLD))
                                .coerceIn(InterCommerceStyles.ALPHA_TRANSPARENT, 
                                    InterCommerceStyles.ALPHA_FULL)
                        }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(InterCommerceStyles.paddingLarge)
                ) {
                    Text(
                        text = product.category.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = InterCommerceStyles.paddingSmall)
                    )
                    Text(
                        text = product.brand,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = InterCommerceStyles.paddingLarge),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(InterCommerceStyles.paddingMedium)
                    ) {
                        Text(
                            text = "$" + String.format(Locale.US, "%.2f", product.priceWithDiscount),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (product.discountPercentage > InterCommerceStyles.ZERO_PERCENTAGE) 
                                InterCommerceStyles.successColor else MaterialTheme.colorScheme.onSurface
                        )
                        if (product.discountPercentage > InterCommerceStyles.ZERO_PERCENTAGE) {
                            Text(
                                text = "$" + String.format(Locale.US, "%.2f", product.price),
                                style = MaterialTheme.typography.titleMedium,
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Gray
                            )
                            Surface(
                                color = InterCommerceStyles.discountBackgroundColor,
                                shape = CircleShape
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.product_detail_discount_format,
                                        "${product.discountPercentage.toInt()}%"
                                    ),
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

                    Spacer(modifier = Modifier.height(InterCommerceStyles.paddingLarge))

                    Text(
                        text = stringResource(R.string.product_detail_description_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = InterCommerceStyles.paddingSmall),
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 
                                InterCommerceStyles.DESCRIPTION_LINE_HEIGHT_FACTOR
                    )

                    val bottomSpacing = innerPadding.calculateBottomPadding() + 
                            InterCommerceStyles.immersiveBottomSpacing
                    Spacer(modifier = Modifier.height(bottomSpacing))
                }
            }

            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = actions.onBackClick,
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(start = InterCommerceStyles.paddingMedium)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.product_detail_back_desc),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    CartBadge(
                        count = cartCount,
                        onCartClick = actions.onCartClick,
                        modifier = Modifier.statusBarsPadding()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        }
    }

    selectedImageIndex?.let { index ->
        ProductImagePreviewDialog(
            images = product.images,
            initialIndex = index,
            onDismiss = { selectedImageIndex = null }
        )
    }
}

@Composable
private fun AddToBagButton(
    state: CartButtonState,
    onClick: () -> Unit
) {
    val loadingAlpha = InterCommerceStyles.BUTTON_LOADING_ALPHA
    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            CartButtonState.Idle -> Color.Black
            CartButtonState.Loading -> Color.Black.copy(alpha = loadingAlpha)
            CartButtonState.Success -> InterCommerceStyles.successColor
        },
        label = "button_color"
    )

    Surface(
        tonalElevation = InterCommerceStyles.shadowElevation,
        shadowElevation = InterCommerceStyles.shadowElevation,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(InterCommerceStyles.paddingLarge)
        ) {
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(InterCommerceStyles.checkoutButtonHeight),
                shape = InterCommerceStyles.buttonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    disabledContainerColor = backgroundColor,
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                ),
                enabled = state == CartButtonState.Idle
            ) {
                AnimatedContent(
                    targetState = state,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "button_content"
                ) { targetState ->
                    when (targetState) {
                        CartButtonState.Idle -> {
                            Text(
                                text = stringResource(R.string.product_detail_add_to_cart),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        CartButtonState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(InterCommerceStyles.loaderSize),
                                color = Color.White,
                                strokeWidth = InterCommerceStyles.loaderStrokeWidth
                            )
                        }
                        CartButtonState.Success -> {
                            Text(
                                text = stringResource(R.string.product_detail_added_to_cart),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
