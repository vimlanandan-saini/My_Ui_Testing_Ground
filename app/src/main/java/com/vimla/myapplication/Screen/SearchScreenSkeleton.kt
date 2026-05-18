package com.vimla.myapplication.Screen

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import com.vimla.myapplication.ui.theme.GrowthGreenBackground
import com.vimla.myapplication.ui.theme.GrowthGreenPrimary
import com.vimla.myapplication.ui.theme.GrowthGreenSecondary
import com.vimla.myapplication.ui.theme.GrowthGreenSurface

// ✅ Skeleton for Instagram-style grid (suggestions)
@Composable
fun SearchScreenSuggestionsSkeleton(innerPadding: PaddingValues,isSmallScreen: Boolean) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Show 4 groups (20 cards total)
        items(4) {
            SkeletonInstagramRow(isSmallScreen = isSmallScreen)
        }
    }
}

// ✅ Skeleton for 3-column grid (search results)
@Composable
fun SearchScreenSearchResultsSkeleton(innerPadding: PaddingValues,isSmallScreen: Boolean) {
    val horizontalPadding = if (isSmallScreen) 16.dp else 20.dp
    val verticalPadding = if (isSmallScreen) 10.dp else 12.dp
    val horizontalSpacing = if (isSmallScreen) 10.dp else 12.dp
    val verticalSpacing = if (isSmallScreen) 14.dp else 16.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthGreenBackground),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = verticalPadding,
            bottom = verticalPadding + 4.dp
        )
    ) {
        items(9) {
            SkeletonSearchResultCard(isSmallScreen = isSmallScreen)
        }
    }
}

// ✅ Skeleton for Instagram mixed pattern row (1 tall + 4 square)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SkeletonInstagramRow(isSmallScreen: Boolean) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val maxWidth = maxWidth
        val columnWidth = (maxWidth - 4.dp) / 3
        val squareHeight = columnWidth
        val tallHeight = squareHeight * 2 + 2.dp

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Tall card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(tallHeight)
            ) {
                SkeletonInstagramTallCard(isSmallScreen = isSmallScreen)
            }

            // Two columns with square cards
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                SkeletonInstagramSquareCard(isSmallScreen = isSmallScreen)
                SkeletonInstagramSquareCard(isSmallScreen = isSmallScreen)
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                SkeletonInstagramSquareCard(isSmallScreen = isSmallScreen)
                SkeletonInstagramSquareCard(isSmallScreen = isSmallScreen)
            }
        }
    }
}

// ✅ Skeleton for Instagram tall card (2:1 aspect ratio)
@Composable
private fun SkeletonInstagramTallCard(isSmallScreen: Boolean) {
    val transition = rememberInfiniteTransition(label = "tallCardShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )

    val pulseAnim by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.15f),
        GrowthGreenPrimary.copy(alpha = pulseAnim),
        Color.White.copy(alpha = pulseAnim * 1.3f),
        GrowthGreenSecondary.copy(alpha = pulseAnim),
        GrowthGreenSurface.copy(alpha = 0.15f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, translateAnim - 500f),
        end = Offset(translateAnim + 500f, translateAnim + 500f)
    )

    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GrowthGreenSurface.copy(alpha = 0.4f))
                .background(brush)
        )
    }
}

// ✅ Skeleton for Instagram square card (1:1 aspect ratio)
@Composable
private fun SkeletonInstagramSquareCard(isSmallScreen: Boolean) {
    val transition = rememberInfiniteTransition(label = "squareCardShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -800f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )

    val pulseAnim by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.15f),
        GrowthGreenPrimary.copy(alpha = pulseAnim),
        Color.White.copy(alpha = pulseAnim * 1.3f),
        GrowthGreenSecondary.copy(alpha = pulseAnim),
        GrowthGreenSurface.copy(alpha = 0.15f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 400f, translateAnim - 400f),
        end = Offset(translateAnim + 400f, translateAnim + 400f)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GrowthGreenSurface.copy(alpha = 0.4f))
                .background(brush)
        )
    }
}

// ✅ Skeleton for 3-column search result card (compact with text)
@Composable
private fun SkeletonSearchResultCard(isSmallScreen: Boolean) {
    val cardShape = if (isSmallScreen) 14.dp else 16.dp
    val innerCardShape = if (isSmallScreen) 10.dp else 12.dp
    val cardTextHeight = if (isSmallScreen) 48.dp else 52.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(cardShape),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
                    .padding(8.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(innerCardShape),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    SearchCardImageShimmer(isSmallScreen = isSmallScreen)
                }
            }

            // Text area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardTextHeight)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SearchCardTextShimmer(widthFraction = 0.8f, delayMillis = 0, isSmallScreen = isSmallScreen)
                    SearchCardTextShimmer(widthFraction = 0.6f, delayMillis = 200, isSmallScreen = isSmallScreen)
                }
            }
        }
    }
}

// ✅ Image shimmer for search result card
@Composable
private fun SearchCardImageShimmer(isSmallScreen: Boolean) {
    val transition = rememberInfiniteTransition(label = "searchCardImageShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -800f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )

    val pulseAnim by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.15f),
        GrowthGreenPrimary.copy(alpha = pulseAnim),
        Color.White.copy(alpha = pulseAnim * 1.3f),
        GrowthGreenSecondary.copy(alpha = pulseAnim),
        GrowthGreenSurface.copy(alpha = 0.15f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 400f, translateAnim - 400f),
        end = Offset(translateAnim + 400f, translateAnim + 400f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthGreenSurface.copy(alpha = 0.4f))
            .background(brush)
    )
}

// ✅ Text shimmer for search result card
@Composable
private fun SearchCardTextShimmer(widthFraction: Float, delayMillis: Int, isSmallScreen: Boolean) {
    val transition = rememberInfiniteTransition(label = "searchCardTextShimmer_$widthFraction")

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
                delayMillis = delayMillis,
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
            .height(14.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(GrowthGreenSurface.copy(alpha = 0.4f))
            .background(brush)
    )
}
