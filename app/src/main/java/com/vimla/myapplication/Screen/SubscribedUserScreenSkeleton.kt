package com.vimla.myapplication.Screen

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp

@Composable
fun SubscribedUserScreenSkeleton(innerPadding: PaddingValues) {
    // ✅ Premium gold colors (matching SubscribedUserScreen theme)
    val premiumGold = Color(0xFFB8860B)
    val lightGold = Color(0xFFFFD700)
    val backgroundColor = Color(0xFFFFF8E1)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFECB3).copy(alpha = 0.3f),
                        backgroundColor,
                        Color(0xFFE8F5E9).copy(alpha = 0.1f)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 12.dp
            )
        ) {
            // ✅ 1. Premium Welcome Header Skeleton
            item {
                SkeletonPremiumHeader(premiumGold, lightGold)
            }

            // ✅ 2. Featured Prompts Grid Skeleton
            item {
                SkeletonFeaturedPromptsCard(premiumGold, lightGold)
            }
        }
    }
}

// ✅ Skeleton for ExpandableHeader (Premium Welcome Card)
@Composable
private fun SkeletonPremiumHeader(premiumGold: Color, lightGold: Color) {
    val transition = rememberInfiniteTransition(label = "headerShimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "headerShimmer"
    )

    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.7f),
        lightGold.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.7f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 1000f, translateAnim - 1000f),
        end = Offset(translateAnim, translateAnim)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ✅ Icon Box (matches ExpandableHeader icon size)
            Surface(
                color = lightGold.copy(alpha = 0.15f),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.size(52.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush)
                )
            }

            // ✅ Text Lines (matches ExpandableHeader text layout)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Title line
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(22.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                // Subtitle line
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }

            // ✅ Arrow Icon (matches ExpandableHeader arrow)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(brush)
            )
        }
    }
}

// ✅ Skeleton for FeaturedPromptsGrid (Main prompts card)
@Composable
private fun SkeletonFeaturedPromptsCard(premiumGold: Color, lightGold: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // ✅ Grid Header (matches FeaturedPromptsGrid header)
            SkeletonGridHeader(premiumGold, lightGold)

            Spacer(Modifier.height(20.dp))

            // ✅ Prompts Grid (3 columns, 9 cards)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 2000.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false,
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(9) {
                    SkeletonPremiumPromptCard(premiumGold)
                }
            }
        }
    }
}

// ✅ Skeleton for Grid Header (Icon + Title + Count)
@Composable
private fun SkeletonGridHeader(premiumGold: Color, lightGold: Color) {
    val transition = rememberInfiniteTransition(label = "gridHeaderShimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.7f),
        lightGold.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.7f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 800f, 0f),
        end = Offset(translateAnim, 0f)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ✅ Icon Box (matches FeaturedPromptsGrid icon)
        Surface(
            color = premiumGold.copy(alpha = 0.15f),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.size(52.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush)
            )
        }

        // ✅ Title + Subtitle
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // "Featured Prompts" title
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            // "X premium prompts" subtitle
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }

        // ✅ Count Badge (matches FeaturedPromptsGrid count)
        Surface(
            color = premiumGold.copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp, 36.dp)
                    .background(brush)
            )
        }
    }
}

// ✅ Skeleton for Premium Prompt Card (matches PromptCard in SubscribedUserScreen)
@Composable
private fun SkeletonPremiumPromptCard(premiumGold: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),  // ✅ EXACT match: Fixed height from PromptCard
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8F4))  // GrowthGreenSurface
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ✅ Image Area (takes remaining space with weight)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    // ✅ Premium Gold Shimmer for image
                    PremiumGoldImageShimmer(premiumGold)
                }
            }

            // ✅ Title Area (fixed 52dp height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                PremiumTitleShimmer(premiumGold)
            }
        }
    }
}

// ✅ Premium Gold Image Shimmer
@Composable
private fun PremiumGoldImageShimmer(premiumGold: Color) {
    val transition = rememberInfiniteTransition(label = "goldImageShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )

    val pulseAnim by transition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val shimmerColors = listOf(
        Color(0xFFFFF8E1).copy(alpha = 0.3f),
        premiumGold.copy(alpha = pulseAnim),
        Color(0xFFFFD700).copy(alpha = pulseAnim * 1.2f),  // Bright gold highlight
        premiumGold.copy(alpha = pulseAnim),
        Color(0xFFFFF8E1).copy(alpha = 0.3f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, translateAnim - 500f),
        end = Offset(translateAnim + 500f, translateAnim + 500f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1).copy(alpha = 0.5f))
            .background(brush)
    )
}


// ✅ Premium Title Shimmer (centered, 2 lines)
@Composable
private fun PremiumTitleShimmer(premiumGold: Color) {
    val transition = rememberInfiniteTransition(label = "titleShimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.6f),
        premiumGold.copy(alpha = 0.3f),
        Color(0xFFFFD700).copy(alpha = 0.4f),
        premiumGold.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.6f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 200f, 0f)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // First line (80% width)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.5f))
                .background(brush)
        )
        // Second line (60% width)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.5f))
                .background(brush)
        )
    }
}
