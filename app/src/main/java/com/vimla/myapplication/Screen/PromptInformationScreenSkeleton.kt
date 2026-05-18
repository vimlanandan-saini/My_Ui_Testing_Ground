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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.vimla.myapplication.ui.theme.GrowthGreenOnSurfaceVariant
import com.vimla.myapplication.ui.theme.GrowthGreenPrimary
import com.vimla.myapplication.ui.theme.GrowthGreenSecondary
import com.vimla.myapplication.ui.theme.GrowthGreenSurface
import com.vimla.myapplication.ui.theme.GrowthGreenTertiary

@Composable
fun PromptInformationScreenSkeleton(
    innerPadding: PaddingValues,
    isSmallScreen: Boolean = false,
    isVerySmallScreen: Boolean = false
) {
    val heroHeight = when {
        isVerySmallScreen -> 340.dp
        isSmallScreen -> 380.dp
        else -> 420.dp
    }
    val heroGradientHeight = when {
        isVerySmallScreen -> 260.dp
        isSmallScreen -> 280.dp
        else -> 300.dp
    }
    val heroCardWidth = when {
        isVerySmallScreen -> 170.dp
        isSmallScreen -> 190.dp
        else -> 200.dp
    }
    val heroCardOffsetY = when {
        isVerySmallScreen -> 52.dp
        isSmallScreen -> 56.dp
        else -> 60.dp
    }
    val heroCardRadius = when {
        isVerySmallScreen -> 16.dp
        isSmallScreen -> 18.dp
        else -> 20.dp
    }
    val screenHorizontalPadding = when {
        isVerySmallScreen -> 16.dp
        isSmallScreen -> 20.dp
        else -> 24.dp
    }
    val contentSectionSpacing = if (isSmallScreen) 24.dp else 32.dp
    val titleHeight = when {
        isVerySmallScreen -> 50.dp
        isSmallScreen -> 56.dp
        else -> 64.dp
    }
    val promptCardRadius = if (isSmallScreen) 14.dp else 16.dp
    val buttonHeight = if (isSmallScreen) 44.dp else 48.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthGreenBackground)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ✅ HERO SECTION - Exact match
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heroHeight)
        ) {
            // Gradient background shimmer
            SkeletonHeroGradient(heroGradientHeight)

            // Image card skeleton
            Box(
                modifier = Modifier
                    .width(heroCardWidth)
                    .aspectRatio(9f / 16f)
                    .align(Alignment.TopCenter)
                    .offset(y = heroCardOffsetY)
            ) {
                SkeletonHeroImageCard(heroCardRadius)
            }
        }

        // ✅ CONTENT SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(GrowthGreenBackground)
                .padding(horizontal = screenHorizontalPadding),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(if (isSmallScreen) 10.dp else 12.dp))

            // ✅ TITLE SKELETON
            SkeletonTitle(titleHeight)

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ BADGE SKELETON ("By Vimlanandan")
            SkeletonBadge(isSmallScreen)

            Spacer(modifier = Modifier.height(if (isSmallScreen) 12.dp else 16.dp))

            // ✅ PROMPT CARD SKELETON (Locked state)
            SkeletonPromptCard(promptCardRadius, isSmallScreen)

            Spacer(modifier = Modifier.height(contentSectionSpacing))

            // ✅ BUTTONS SKELETON
            SkeletonButtonRow(buttonHeight, isSmallScreen)

            Spacer(modifier = Modifier.height(if (isSmallScreen) 6.dp else 8.dp))

            // ✅ HELPER TEXT SKELETON
            SkeletonHelperText()

            Spacer(modifier = Modifier.height(if (isSmallScreen) 32.dp else 48.dp))

            // ✅ RECOMMENDATIONS SECTION
            SkeletonRecommendationsSection(isSmallScreen)

            Spacer(modifier = Modifier.height(if (isSmallScreen) 32.dp else 48.dp))
        }
    }
}

@Composable
private fun SkeletonHeroGradient(height: Dp) {
    val transition = rememberInfiniteTransition(label = "heroGradient")

    val translateAnim by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "heroSweep"
    )

    val pulseAnim by transition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heroPulse"
    )

    val shimmerColors = listOf(
        GrowthGreenPrimary.copy(alpha = pulseAnim),
        GrowthGreenSecondary.copy(alpha = pulseAnim * 0.8f),
        GrowthGreenTertiary.copy(alpha = pulseAnim * 0.6f),
        GrowthGreenBackground
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.verticalGradient(
                    colors = shimmerColors,
                    startY = 0f,
                    endY = height.value * 3f
                )
            )
    )
}

@Composable
private fun SkeletonHeroImageCard(radius: Dp) {
    val transition = rememberInfiniteTransition(label = "heroImageCard")

    val translateAnim by transition.animateFloat(
        initialValue = -800f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cardSweep"
    )

    val pulseAnim by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cardPulse"
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
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(radius),
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GrowthGreenSurface.copy(alpha = 0.4f))
                .background(brush)
        )
    }
}

@Composable
private fun SkeletonTitle(height: Dp) {
    val transition = rememberInfiniteTransition(label = "titleShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "titleTranslate"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.9f),
        GrowthGreenSurface.copy(alpha = 0.3f),
        GrowthGreenSurface.copy(alpha = 0.9f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 300f, 0f)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // First title line
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(brush)
        )
        // Second title line (shorter)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(brush)
        )
    }
}

@Composable
private fun SkeletonBadge(isSmallScreen: Boolean) {
    val transition = rememberInfiniteTransition(label = "badgeShimmer")

    val pulseAnim by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "badgePulse"
    )

    val shimmerColors = listOf(
        GrowthGreenPrimary.copy(alpha = 0.08f),
        GrowthGreenPrimary.copy(alpha = pulseAnim),
        GrowthGreenPrimary.copy(alpha = 0.08f)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(
            color = GrowthGreenPrimary.copy(alpha = 0.12f),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(if (isSmallScreen) 160.dp else 180.dp)
                    .height(if (isSmallScreen) 32.dp else 36.dp)
                    .background(
                        brush = Brush.horizontalGradient(shimmerColors)
                    )
            )
        }
    }
}

@Composable
private fun SkeletonPromptCard(radius: Dp, isSmallScreen: Boolean) {
    val transition = rememberInfiniteTransition(label = "promptCardShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -500f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "promptTranslate"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.9f),
        GrowthGreenOnSurfaceVariant.copy(alpha = 0.2f),
        GrowthGreenSurface.copy(alpha = 0.9f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 500f, 0f)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface),
        shape = RoundedCornerShape(radius),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            GrowthGreenOnSurfaceVariant.copy(alpha = 0.15f),
                            GrowthGreenOnSurfaceVariant.copy(alpha = 0.08f),
                            GrowthGreenOnSurfaceVariant.copy(alpha = 0.15f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(if (isSmallScreen) 28.dp else 32.dp)
            ) {
                // Lock icon shimmer
                Box(
                    modifier = Modifier
                        .size(if (isSmallScreen) 56.dp else 64.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    GrowthGreenPrimary.copy(alpha = 0.2f),
                                    GrowthGreenPrimary.copy(alpha = 0.1f)
                                )
                            )
                        )
                )

                Spacer(Modifier.height(if (isSmallScreen) 12.dp else 16.dp))

                // Text line shimmer
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }
        }
    }
}

@Composable
private fun SkeletonButtonRow(buttonHeight: Dp, isSmallScreen: Boolean) {
    val transition = rememberInfiniteTransition(label = "buttonRowShimmer")

    val pulseAnim by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "buttonPulse"
    )

    // ✅ SINGLE BUTTON - Locked state shows only "Copy Prompt" button
    SkeletonButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight),
        pulseAlpha = pulseAnim,
        delayMillis = 0
    )
}

@Composable
private fun SkeletonButton(
    modifier: Modifier,
    pulseAlpha: Float,
    delayMillis: Int
) {
    val transition = rememberInfiniteTransition(label = "buttonShimmer_$delayMillis")

    val translateAnim by transition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1300,
                delayMillis = delayMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "buttonTranslate"
    )

    val shimmerColors = listOf(
        GrowthGreenPrimary.copy(alpha = pulseAlpha * 0.6f),
        GrowthGreenPrimary.copy(alpha = pulseAlpha),
        GrowthGreenPrimary.copy(alpha = pulseAlpha * 0.6f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 200f, 0f)
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = GrowthGreenPrimary.copy(alpha = 0.15f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush)
        )
    }
}


@Composable
private fun SkeletonHelperText() {
    val transition = rememberInfiniteTransition(label = "helperTextShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "helperTranslate"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.8f),
        GrowthGreenSurface.copy(alpha = 0.3f),
        GrowthGreenSurface.copy(alpha = 0.8f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 200f, 0f)
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
        )
    }
}

@Composable
private fun SkeletonRecommendationsSection(isSmallScreen: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(if (isSmallScreen) 16.dp else 20.dp)
    ) {
        // Section header
        SkeletonRecommendationsHeader()

        // Recommendation cards grid
        SkeletonRecommendationsGrid()
    }
}

@Composable
private fun SkeletonRecommendationsHeader() {
    val transition = rememberInfiniteTransition(label = "recHeaderShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -250f,
        targetValue = 250f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "recHeaderTranslate"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.9f),
        GrowthGreenSurface.copy(alpha = 0.3f),
        GrowthGreenSurface.copy(alpha = 0.9f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 250f, 0f)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Icon shimmer
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(brush)
        )

        // Title shimmer
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
        )
    }
}

@Composable
private fun SkeletonRecommendationsGrid() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(4) {
            SkeletonRecommendationCard()
        }
    }
}

@Composable
private fun SkeletonRecommendationCard() {
    Card(
        modifier = Modifier
            .width(120.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
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
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface)
                ) {
                    PremiumCardImageShimmer()
                }
            }

            // Text area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                CardTextShimmer()
            }
        }
    }
}

@Composable
private fun PremiumCardImageShimmer() {
    val transition = rememberInfiniteTransition(label = "cardImageShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -800f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )

    val pulseAnim by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
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

@Composable
private fun CardTextShimmer() {
    val transition = rememberInfiniteTransition(label = "cardTextShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -150f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f)),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val alphaAnim by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
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
            .fillMaxWidth(0.8f)
            .height(14.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(GrowthGreenSurface.copy(alpha = 0.4f))
            .background(brush)
    )
}
