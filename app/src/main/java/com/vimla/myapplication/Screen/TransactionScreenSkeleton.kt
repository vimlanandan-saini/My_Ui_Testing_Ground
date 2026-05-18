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
import androidx.compose.material3.HorizontalDivider
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
import com.vimla.myapplication.ui.theme.GrowthGreenBorder
import com.vimla.myapplication.ui.theme.GrowthGreenOnBackground
import com.vimla.myapplication.ui.theme.GrowthGreenOnSurfaceVariant
import com.vimla.myapplication.ui.theme.GrowthGreenPrimary
import com.vimla.myapplication.ui.theme.GrowthGreenSuccess
import com.vimla.myapplication.ui.theme.GrowthGreenSurface

// ====================================================================
// SKELETON LOADING STATE
// ====================================================================

// ====================================================================
// SKELETON LOADING STATE
// ====================================================================

@Composable
fun TransactionScreenSkeleton(innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthGreenBackground)
            .padding(innerPadding),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),  // ✅ Same as TransactionsListContent
        verticalArrangement = Arrangement.spacedBy(16.dp)  // ✅ Same spacing
    ) {
        // ✅ Header card skeleton
        item {
            SkeletonTransactionHeader()
        }

        // ✅ Show 6 transaction card skeletons
        items(6) {
            SkeletonTransactionCard()
        }
    }
}

// ====================================================================
// SKELETON: HEADER CARD
// ====================================================================

@Composable
private fun SkeletonTransactionHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = GrowthGreenPrimary.copy(alpha = 0.08f)  // ✅ Same as real header
        ),
        shape = RoundedCornerShape(16.dp)  // ✅ Same radius
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),  // ✅ Same padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ✅ Left: Title + Subtitle shimmers
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                HeaderTitleShimmer()
                HeaderSubtitleShimmer()
            }

            // ✅ Right: Icon shimmer
            HeaderIconShimmer()
        }
    }
}

@Composable
private fun HeaderTitleShimmer() {
    val transition = rememberInfiniteTransition(label = "headerTitle")
    val translateAnim by transition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = listOf(
        GrowthGreenPrimary.copy(alpha = 0.15f),
        GrowthGreenPrimary.copy(alpha = 0.35f),
        GrowthGreenPrimary.copy(alpha = 0.15f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 200f, 0f)
    )

    Box(
        modifier = Modifier
            .width(140.dp)  // ✅ "Transaction History"
            .height(16.dp)  // ✅ 16.sp text height
            .clip(RoundedCornerShape(4.dp))
            .background(brush)
    )
}

@Composable
private fun HeaderSubtitleShimmer() {
    val transition = rememberInfiniteTransition(label = "headerSubtitle")
    val translateAnim by transition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                delayMillis = 200,  // ✅ Staggered
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = listOf(
        GrowthGreenPrimary.copy(alpha = 0.12f),
        GrowthGreenPrimary.copy(alpha = 0.28f),
        GrowthGreenPrimary.copy(alpha = 0.12f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 200f, 0f)
    )

    Box(
        modifier = Modifier
            .width(80.dp)  // ✅ "X transactions"
            .height(14.dp)  // ✅ 14.sp text height
            .clip(RoundedCornerShape(4.dp))
            .background(brush)
    )
}

@Composable
private fun HeaderIconShimmer() {
    val transition = rememberInfiniteTransition(label = "headerIcon")

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

    Box(
        modifier = Modifier
            .size(24.dp)  // ✅ Same as real icon
            .clip(CircleShape)
            .background(GrowthGreenPrimary.copy(alpha = pulseAnim))
    )
}

// ====================================================================
// SKELETON: TRANSACTION CARD
// ====================================================================

@Composable
private fun SkeletonTransactionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),  // ✅ Same elevation
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface),
        shape = RoundedCornerShape(16.dp)  // ✅ Same radius
    ) {
        Column(Modifier.padding(20.dp)) {  // ✅ Same padding
            // ✅ Header row: Plan name + Status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TransactionTitleShimmer()
                StatusBadgeShimmer()
            }

            Spacer(Modifier.height(12.dp))  // ✅ Same spacing

            // ✅ Price
            PriceShimmer()

            Spacer(Modifier.height(6.dp))  // ✅ Same spacing

            // ✅ Date row
            Row(verticalAlignment = Alignment.CenterVertically) {
                DateIconShimmer()
                Spacer(Modifier.width(6.dp))
                DateTextShimmer()
            }

            Spacer(Modifier.height(12.dp))  // ✅ Same spacing
            HorizontalDivider(color = GrowthGreenBorder)  // ✅ Same as real divider
            Spacer(Modifier.height(12.dp))  // ✅ Same spacing

            // ✅ Source badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SourceBadgeShimmer()
                // ✅ 50% chance to show second badge (auto-renew)
                if (Math.random() > 0.5) {
                    SecondaryBadgeShimmer()
                }
            }
        }
    }
}

// ====================================================================
// TRANSACTION CARD COMPONENTS
// ====================================================================

@Composable
private fun TransactionTitleShimmer() {
    val transition = rememberInfiniteTransition(label = "transactionTitle")
    val translateAnim by transition.animateFloat(
        initialValue = -250f,
        targetValue = 250f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.9f),
        GrowthGreenOnBackground.copy(alpha = 0.25f),
        Color.White.copy(alpha = 0.15f),
        GrowthGreenOnBackground.copy(alpha = 0.25f),
        GrowthGreenSurface.copy(alpha = 0.9f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 125f, 0f),
        end = Offset(translateAnim + 125f, 0f)
    )

    Box(
        modifier = Modifier
            .width(140.dp)  // ✅ ~60% of card width
            .height(18.dp)  // ✅ 18.sp text height
            .clip(RoundedCornerShape(4.dp))
            .background(GrowthGreenSurface.copy(alpha = 0.4f))
            .background(brush)
    )
}

@Composable
private fun StatusBadgeShimmer() {
    val transition = rememberInfiniteTransition(label = "statusBadge")
    val pulseAnim by transition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.30f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Surface(
        color = GrowthGreenSuccess.copy(alpha = pulseAnim),
        shape = RoundedCornerShape(8.dp)  // ✅ Same as status badge
    ) {
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(20.dp)
                .padding(horizontal = 10.dp, vertical = 4.dp)  // ✅ Same padding
        )
    }
}

@Composable
private fun PriceShimmer() {
    val transition = rememberInfiniteTransition(label = "price")
    val translateAnim by transition.animateFloat(
        initialValue = -150f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = listOf(
        GrowthGreenPrimary.copy(alpha = 0.15f),
        GrowthGreenPrimary.copy(alpha = 0.40f),
        Color.White.copy(alpha = 0.25f),
        GrowthGreenPrimary.copy(alpha = 0.40f),
        GrowthGreenPrimary.copy(alpha = 0.15f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 75f, 0f),
        end = Offset(translateAnim + 75f, 0f)
    )

    Box(
        modifier = Modifier
            .width(80.dp)  // ✅ Price text width
            .height(16.dp)  // ✅ 16.sp text height
            .clip(RoundedCornerShape(4.dp))
            .background(GrowthGreenPrimary.copy(alpha = 0.1f))
            .background(brush)
    )
}

@Composable
private fun DateIconShimmer() {
    val transition = rememberInfiniteTransition(label = "dateIcon")
    val pulseAnim by transition.animateFloat(
        initialValue = 0.20f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .size(16.dp)  // ✅ Same as date icon
            .clip(CircleShape)
            .background(GrowthGreenOnSurfaceVariant.copy(alpha = pulseAnim))
    )
}

@Composable
private fun DateTextShimmer() {
    val transition = rememberInfiniteTransition(label = "dateText")
    val translateAnim by transition.animateFloat(
        initialValue = -150f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                delayMillis = 100,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.8f),
        GrowthGreenOnSurfaceVariant.copy(alpha = 0.35f),
        GrowthGreenSurface.copy(alpha = 0.8f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 150f, 0f)
    )

    Box(
        modifier = Modifier
            .width(120.dp)  // ✅ Date text width
            .height(13.dp)  // ✅ 13.sp text height
            .clip(RoundedCornerShape(4.dp))
            .background(brush)
    )
}

@Composable
private fun SourceBadgeShimmer() {
    val transition = rememberInfiniteTransition(label = "sourceBadge")
    val pulseAnim by transition.animateFloat(
        initialValue = 0.10f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Surface(
        color = Color(0xFF4285F4).copy(alpha = pulseAnim),
        shape = RoundedCornerShape(6.dp)  // ✅ Same as source badge
    ) {
        Box(
            modifier = Modifier
                .width(90.dp)  // ✅ "Google Play" width
                .height(22.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp)  // ✅ Same padding
        )
    }
}

@Composable
private fun SecondaryBadgeShimmer() {
    val transition = rememberInfiniteTransition(label = "secondaryBadge")
    val pulseAnim by transition.animateFloat(
        initialValue = 0.10f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600,
                delayMillis = 200,  // ✅ Staggered
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Surface(
        color = GrowthGreenSuccess.copy(alpha = pulseAnim),
        shape = RoundedCornerShape(6.dp)
    ) {
        Box(
            modifier = Modifier
                .width(75.dp)  // ✅ "Auto-Renew" width
                .height(22.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
