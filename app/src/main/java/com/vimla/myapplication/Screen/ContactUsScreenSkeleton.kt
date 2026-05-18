package com.vimla.myapplication.Screen

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vimla.myapplication.ui.theme.GrowthGreenBackground
import com.vimla.myapplication.ui.theme.GrowthGreenPrimary
import com.vimla.myapplication.ui.theme.GrowthGreenSecondary
import com.vimla.myapplication.ui.theme.GrowthGreenSurface

@Composable
 fun ContactUsScreenSkeleton(
    innerPadding: PaddingValues,
    horizontalPadding: Dp,
    verticalPadding: Dp,
    cardSpacing: Dp,
    isSmallScreen: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalArrangement = Arrangement.spacedBy(cardSpacing)
    ) {
        // Header skeleton
        item { SkeletonContactHeader(isSmallScreen = isSmallScreen) }

        // Contact cards skeleton (3 rows of 2 cards = 6 cards)
        items(3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(if (isSmallScreen) 8.dp else 12.dp)
            ) {
                SkeletonContactCard(
                    modifier = Modifier.weight(1f),
                    isSmallScreen = isSmallScreen
                )
                SkeletonContactCard(
                    modifier = Modifier.weight(1f),
                    isSmallScreen = isSmallScreen
                )
            }
        }
    }
}

@Composable
private fun SkeletonContactHeader(isSmallScreen: Boolean) {
    val headerPadding = if (isSmallScreen) 16.dp else 20.dp
    val iconSize = if (isSmallScreen) 60.dp else 70.dp
    val iconInnerSize = if (isSmallScreen) 30.dp else 36.dp

    val transition = rememberInfiniteTransition(label = "headerShimmer")

    val pulseAnim by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val rotateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(if (isSmallScreen) 16.dp else 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GrowthGreenPrimary.copy(alpha = 0.15f),
                            GrowthGreenSecondary.copy(alpha = 0.15f),
                            Color(0xFF8B5CF6).copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(if (isSmallScreen) 16.dp else 20.dp)
                )
                .padding(headerPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()  // ✅ ADD THIS
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(if (isSmallScreen) 10.dp else 12.dp)
            ) {

                // Icon shimmer
                Box(
                    modifier = Modifier
                        .size(iconSize)
                        .background(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    GrowthGreenPrimary.copy(alpha = 0.1f),
                                    GrowthGreenPrimary.copy(alpha = pulseAnim),
                                    GrowthGreenSecondary.copy(alpha = pulseAnim),
                                    Color(0xFF8B5CF6).copy(alpha = pulseAnim),
                                    GrowthGreenPrimary.copy(alpha = 0.1f)
                                ),
                                center = Offset(iconSize.value / 2, iconSize.value / 2)
                            ),
                            shape = CircleShape
                        )
                )

                Spacer(modifier = Modifier.height(if (isSmallScreen) 4.dp else 4.dp))

                // Title shimmer
                HeaderTextShimmer(
                    width = if (isSmallScreen) 120.dp else 140.dp,
                    height = if (isSmallScreen) 18.dp else 20.dp,
                    delayMillis = 0
                )

                Spacer(modifier = Modifier.height(if (isSmallScreen) 2.dp else 0.dp))

                // Description shimmer (2 lines)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    HeaderTextShimmer(
                        width = if (isSmallScreen) 200.dp else 240.dp,
                        height = if (isSmallScreen) 12.dp else 13.dp,
                        delayMillis = 100
                    )
                    HeaderTextShimmer(
                        width = if (isSmallScreen) 160.dp else 200.dp,
                        height = if (isSmallScreen) 12.dp else 13.dp,
                        delayMillis = 200
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderTextShimmer(width: Dp, height: Dp, delayMillis: Int) {
    val transition = rememberInfiniteTransition(label = "headerTextShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                delayMillis = delayMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.4f),
        GrowthGreenSurface.copy(alpha = 0.15f),
        GrowthGreenSurface.copy(alpha = 0.4f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 200f, 0f)
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .background(brush)
    )
}

@Composable
private fun SkeletonContactCard(
    modifier: Modifier = Modifier,
    isSmallScreen: Boolean
) {
    val cardPadding = if (isSmallScreen) 12.dp else 14.dp
    val iconBoxSize = if (isSmallScreen) 48.dp else 56.dp
    val iconSize = if (isSmallScreen) 40.dp else 48.dp

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(if (isSmallScreen) 14.dp else 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(cardPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(if (isSmallScreen) 8.dp else 10.dp)
        ) {
            // Icon shimmer
            ContactIconShimmer(
                size = iconBoxSize,
                iconSize = iconSize,
                isSmallScreen = isSmallScreen
            )

            // Name text shimmer (2 lines)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                ContactTextShimmer(
                    widthFraction = 0.7f,
                    height = if (isSmallScreen) 12.dp else 13.dp,
                    delayMillis = 0
                )
            }

            // Action button shimmer
            ContactActionShimmer(isSmallScreen = isSmallScreen)
        }
    }
}

@Composable
private fun ContactIconShimmer(size: Dp, iconSize: Dp, isSmallScreen: Boolean) {
    val transition = rememberInfiniteTransition(label = "contactIconShimmer")

    val pulseAnim by transition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val rotateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 4000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate"
    )

    Box(
        modifier = Modifier
            .size(size)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        GrowthGreenPrimary.copy(alpha = 0.15f),
                        GrowthGreenSecondary.copy(alpha = 0.15f)
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            GrowthGreenSurface.copy(alpha = 0.1f),
                            GrowthGreenPrimary.copy(alpha = pulseAnim),
                            Color.White.copy(alpha = pulseAnim * 0.5f),
                            GrowthGreenPrimary.copy(alpha = pulseAnim),
                            GrowthGreenSurface.copy(alpha = 0.1f)
                        ),
                        center = Offset(iconSize.value / 2, iconSize.value / 2)
                    ),
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun ContactTextShimmer(widthFraction: Float, height: Dp, delayMillis: Int) {
    val transition = rememberInfiniteTransition(label = "contactTextShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -150f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1300,
                delayMillis = delayMillis,
                easing = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f)
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val alphaAnim by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.3f),
        GrowthGreenPrimary.copy(alpha = alphaAnim),
        Color.White.copy(alpha = alphaAnim * 0.7f),
        GrowthGreenPrimary.copy(alpha = alphaAnim),
        GrowthGreenSurface.copy(alpha = 0.3f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 150f, 0f)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .background(GrowthGreenSurface.copy(alpha = 0.4f))
            .background(brush)
    )
}

@Composable
private fun ContactActionShimmer(isSmallScreen: Boolean) {
    val transition = rememberInfiniteTransition(label = "actionShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -100f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                delayMillis = 300,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = listOf(
        GrowthGreenPrimary.copy(alpha = 0.05f),
        GrowthGreenPrimary.copy(alpha = 0.15f),
        GrowthGreenPrimary.copy(alpha = 0.05f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 100f, 0f)
    )

    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(if (isSmallScreen) 14.dp else 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isSmallScreen) 26.dp else 30.dp)
                .background(brush)
        )
    }
}
