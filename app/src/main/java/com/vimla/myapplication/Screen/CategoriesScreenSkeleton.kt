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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.vimla.myapplication.ui.theme.GrowthGreenSecondary
import com.vimla.myapplication.ui.theme.GrowthGreenSuccess
import com.vimla.myapplication.ui.theme.GrowthGreenSurface
import com.vimla.myapplication.ui.theme.GrowthGreenSurfaceVariant

@Composable
fun CategoriesScreenSkeleton(innerPadding: PaddingValues) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthGreenBackground)
    ) {
        // ✅ Left Sidebar: 100dp width
        SkeletonCategorySidebar(
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
        )

        // ✅ Right Content Area
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            // ✅ Subcategories Row
            SkeletonSubcategoriesRow(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            )

            // ✅ Prompts Grid
            SkeletonPromptsGrid(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

// ====================================================================
// CATEGORY SIDEBAR SKELETON
// ====================================================================

@Composable
private fun SkeletonCategorySidebar(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.background(GrowthGreenSurface),
        contentPadding = PaddingValues(vertical = 12.dp),  // ✅ Same as CategorySidebar
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(6) { index ->  // ✅ Show 6 skeleton categories
            SkeletonCategoryItem(isFirst = index == 0)
        }
    }
}

@Composable
private fun SkeletonCategoryItem(isFirst: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),  // ✅ Same as CategorySideItem
        contentAlignment = Alignment.Center
    ) {
        // ✅ EXACT MATCH: Selection indicator bar (shown for first item)
        if (isFirst) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(4.dp)  // ✅ Same width
                    .height(50.dp)  // ✅ Same height
                    .background(
                        GrowthGreenPrimary.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)  // ✅ Same spacing
        ) {
            // ✅ EXACT MATCH: 60dp circular category icon
            SkeletonCategoryIcon(isSelected = isFirst)

            // ✅ EXACT MATCH: Category name text (2 lines)
            SkeletonCategoryText()
        }
    }
}

@Composable
private fun SkeletonCategoryIcon(isSelected: Boolean) {
    val transition = rememberInfiniteTransition(label = "categoryIconShimmer")

    val pulseAnim by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.32f,
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
                durationMillis = 3500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.2f),
        GrowthGreenPrimary.copy(alpha = pulseAnim),
        Color.White.copy(alpha = pulseAnim * 1.2f),
        GrowthGreenPrimary.copy(alpha = pulseAnim),
        GrowthGreenSurface.copy(alpha = 0.2f)
    )

    Card(
        modifier = Modifier.size(60.dp),  // ✅ EXACT SIZE
        shape = CircleShape,  // ✅ EXACT SHAPE
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GrowthGreenSurface else GrowthGreenSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp  // ✅ EXACT ELEVATION
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.sweepGradient(
                        colors = shimmerColors,
                        center = Offset(30f, 30f)
                    )
                )
        )
    }
}

@Composable
private fun SkeletonCategoryText() {
    val transition = rememberInfiniteTransition(label = "categoryTextShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -100f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1300,
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
        end = Offset(translateAnim + 100f, 0f)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // ✅ First line (longer)
        Box(
            modifier = Modifier
                .width(56.dp)
                .height(12.dp)  // ✅ bodySmall ≈ 12sp
                .clip(RoundedCornerShape(3.dp))
                .background(brush)
        )

        // ✅ Second line (shorter)
        Box(
            modifier = Modifier
                .width(42.dp)
                .height(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(brush)
        )
    }
}

// ====================================================================
// SUBCATEGORIES ROW SKELETON
// ====================================================================

@Composable
private fun SkeletonSubcategoriesRow(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)  // ✅ EXACT SPACING
    ) {
        items(5) { index ->  // ✅ Show 5 skeleton chips (including "All")
            SkeletonFilterChip(isFirst = index == 0)
        }
    }
}

@Composable
private fun SkeletonFilterChip(isFirst: Boolean = false) {
    val transition = rememberInfiniteTransition(label = "chipShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -150f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                delayMillis = if (isFirst) 0 else 200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = if (isFirst) {
        // "All" chip (selected state)
        listOf(
            GrowthGreenPrimary.copy(alpha = 0.5f),
            GrowthGreenPrimary.copy(alpha = 0.25f),
            GrowthGreenPrimary.copy(alpha = 0.5f)
        )
    } else {
        // Regular chips
        listOf(
            GrowthGreenSurfaceVariant.copy(alpha = 0.8f),
            GrowthGreenSurfaceVariant.copy(alpha = 0.4f),
            GrowthGreenSurfaceVariant.copy(alpha = 0.8f)
        )
    }

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 150f, 0f)
    )

    Surface(
        shape = RoundedCornerShape(8.dp),  // ✅ MaterialTheme.shapes.medium ≈ 8dp
        color = if (isFirst) GrowthGreenPrimary.copy(alpha = 0.3f) else GrowthGreenSurfaceVariant,
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .width(if (isFirst) 60.dp else (70..90).random().dp)  // Variable widths
                .height(32.dp)  // ✅ Standard FilterChip height
                .background(brush)
        )
    }
}

// ====================================================================
// PROMPTS GRID SKELETON
// ====================================================================

@Composable
private fun SkeletonPromptsGrid(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),  // ✅ EXACT: 2 columns
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),  // ✅ EXACT SPACING
        horizontalArrangement = Arrangement.spacedBy(12.dp),  // ✅ EXACT SPACING
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)  // ✅ EXACT PADDING
    ) {
        items(10) { index ->  // ✅ CHANGED: 6 → 10 cards (5 rows, allows scrolling)
            SkeletonPromptCard(index = index)
        }
    }
}
@Composable
private fun SkeletonPromptCard(index: Int = 0) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f),  // ✅ EXACT ASPECT RATIO
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),  // ✅ EXACT ELEVATION
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface),
        shape = RoundedCornerShape(16.dp)  // ✅ EXACT RADIUS
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // ✅ Premium shimmer for image area (staggered animation)
            SkeletonPromptImage(delayMillis = index * 100)

            // ✅ Gender indicator dot (bottom right)
            SkeletonGenderDot(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)  // ✅ EXACT PADDING
            )
        }
    }
}

@Composable
private fun SkeletonPromptImage(delayMillis: Int = 0) {
    val transition = rememberInfiniteTransition(label = "promptImageShimmer_$delayMillis")

    // Diagonal sweep animation
    val translateAnim by transition.animateFloat(
        initialValue = -1200f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                delayMillis = delayMillis,  // ✅ Staggered animation
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )

    // Pulsing brightness
    val pulseAnim by transition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600,
                delayMillis = delayMillis,  // ✅ Staggered animation
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.2f),
        GrowthGreenPrimary.copy(alpha = pulseAnim),
        Color.White.copy(alpha = pulseAnim * 1.4f),  // Bright highlight
        GrowthGreenSecondary.copy(alpha = pulseAnim * 0.9f),
        GrowthGreenPrimary.copy(alpha = pulseAnim * 0.7f),
        GrowthGreenSurface.copy(alpha = 0.2f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 600f, translateAnim - 600f),
        end = Offset(translateAnim + 600f, translateAnim + 600f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthGreenSurface.copy(alpha = 0.4f))  // Base layer
            .background(brush)  // Shimmer layer
    )
}

@Composable
private fun SkeletonGenderDot(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "genderDotShimmer")

    val alphaAnim by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Surface(
        modifier = modifier.size(8.dp),  // ✅ Standard gender dot size
        shape = CircleShape,
        color = GrowthGreenSurfaceVariant.copy(alpha = alphaAnim)
    ) {}
}