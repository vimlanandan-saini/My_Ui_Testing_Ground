package com.vimla.myapplication

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.razorpay.Checkout
import com.vimla.myapplication.Screen.PromotionVideoMakingScreen
import com.vimla.myapplication.ui.theme.GrowthGreenSurfaceVariant
import com.vimla.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Checkout.preload(applicationContext)

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PromotionVideoMakingScreen(
                    onOrderNowClick = {
                        // TODO: launch your Razorpay Checkout / order flow here
                    }
                )
            }
        }
    }
}


@Composable
private fun LoadingSuggestionsGrid(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        GrowthGreenSurfaceVariant,
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// SKELETON LOADING CONTENT (PIXEL-PERFECT MATCH)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun FavoritesSkeletonContent(modifier: Modifier = Modifier) {

    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthGreenBackground),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 12.dp,
            bottom = 16.dp
        )
    ) {
        item {
            SkeletonFavoritesHeader()
        }

        item {
            SkeletonFavoritesGrid()
        }
    }
}

@Composable
private fun SkeletonFavoritesHeader() {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)

    val transition = rememberInfiniteTransition(label = "headerShimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "headerShimmer"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.9f),
        GrowthGreenSurface.copy(alpha = 0.3f),
        GrowthGreenSurface.copy(alpha = 0.9f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 1000f, translateAnim - 1000f),
        end = Offset(translateAnim, translateAnim)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(brush)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }

            Box(
                modifier = Modifier
                    .size(52.dp, 36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush)
            )
        }
    }
}

@Composable
private fun SkeletonFavoritesGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 5000.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false,
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(9) {
            SkeletonFavoriteCard()
        }
    }
}

// ✅ PIXEL-PERFECT SKELETON CARD (matches CompactFavoriteCard exactly)
@Composable
private fun SkeletonFavoriteCard() {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ✅ EXACT MATCH: Image area with same padding and aspect ratio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
                    .padding(8.dp)  // Same padding as CompactFavoriteCard
            ) {
                // ✅ EXACT MATCH: Inner Card with same shape and elevation
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(12.dp),  // Same radius
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),  // Same elevation
                    colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface)
                ) {
                    // ✅ Premium shimmer effect for image
                    PremiumImageShimmer()
                }
            }

            // ✅ EXACT MATCH: Text area with same dimensions
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)  // Same height
                    .padding(horizontal = 10.dp, vertical = 4.dp),  // Same padding
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ✅ First text line shimmer
                    WaveTextShimmer(widthFraction = 0.8f, delayMillis = 0)

                    // ✅ Second text line shimmer
                    WaveTextShimmer(widthFraction = 0.6f, delayMillis = 200)
                }
            }
        }
    }
}

// ✅ Premium image shimmer (diagonal sweep + glow + pulse)
@Composable
private fun PremiumImageShimmer() {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)

    val transition = rememberInfiniteTransition(label = "premiumImageShimmer")

    // Diagonal sweep animation
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

    // Pulsing brightness
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

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.2f),
        GrowthGreenPrimary.copy(alpha = pulseAnim),
        Color.White.copy(alpha = pulseAnim * 1.5f),  // Bright highlight
        GrowthGreenSecondary.copy(alpha = pulseAnim),
        GrowthGreenSurface.copy(alpha = 0.2f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, translateAnim - 500f),
        end = Offset(translateAnim + 500f, translateAnim + 500f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthGreenSurface.copy(alpha = 0.5f))  // Base layer
            .background(brush)  // Shimmer layer
    )
}

// ✅ Wave text shimmer (horizontal wave with staggered animation)
@Composable
private fun WaveTextShimmer(widthFraction: Float, delayMillis: Int) {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)
    val transition = rememberInfiniteTransition(label = "waveTextShimmer_$widthFraction")

    val translateAnim by transition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                delayMillis = delayMillis,
                easing = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f)
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveTranslate"
    )

    val alphaAnim by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                delayMillis = delayMillis,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveAlpha"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.3f),
        GrowthGreenPrimary.copy(alpha = alphaAnim),
        Color.White.copy(alpha = alphaAnim * 0.8f),
        GrowthGreenPrimary.copy(alpha = alphaAnim),
        GrowthGreenSurface.copy(alpha = 0.3f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 300f, 0f)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(14.dp)  // ✅ Matches text line height in CompactFavoriteCard
            .clip(RoundedCornerShape(4.dp))
            .background(GrowthGreenSurface.copy(alpha = 0.4f))  // Base
            .background(brush)  // Shimmer
    )
}


@Composable
fun AppVersion(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Get version information
    var versionName: String? = "Unknown"
    var versionCode = 0

    try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        versionName = packageInfo.versionName
        versionCode = packageInfo.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    // Display version info
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dodo Bird App",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Version Name: $versionName",
            fontSize = 18.sp,
            modifier = Modifier.padding(4.dp)
        )

        Text(
            text = "Version Code: $versionCode",
            fontSize = 18.sp,
            modifier = Modifier.padding(4.dp)
        )
    }
}


//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatDelegate
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.lifecycleScope
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.rememberNavController
//import com.android.billingclient.api.BillingClient
//import com.android.billingclient.api.QueryPurchasesParams
//import com.vimla.prompttubeai.Navigation.NavGraph
//import com.vimla.prompttubeai.Screen.ForceUpdateScreen
//import com.vimla.prompttubeai.Screen.MaintenanceScreen
//import com.vimla.prompttubeai.ViewModel.AppConfigAndLimitViewModel
//import com.vimla.prompttubeai.ViewModel.AuthViewModel
//import com.vimla.prompttubeai.ViewModel.BillingManager
//import com.vimla.prompttubeai.ViewModel.CategoriesViewModel
//import com.vimla.prompttubeai.ui.components.LoadingIndicator
//import com.vimla.prompttubeai.ui.theme.GrowthGreenBackground
//import com.vimla.prompttubeai.ui.theme.GrowthGreenPrimary
//import com.vimla.prompttubeai.ui.theme.GrowthGreenSecondary
//import com.vimla.prompttubeai.ui.theme.PromptTubeAiTheme
//import com.vimla.prompttubeai.utils.AppTimeTracker
//import com.vimla.prompttubeai.utils.InAppReviewManager
//import com.vimla.prompttubeai.utils.SubscriptionUtils
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import org.bouncycastle.crypto.params.Blake3Parameters.context
//
//class MainActivity : ComponentActivity() {
//
//    private lateinit var reviewManager: InAppReviewManager
//    private lateinit var billingManager: BillingManager
//    private val authViewModel: AuthViewModel by viewModels()
//    val categoriesViewModel: CategoriesViewModel by viewModels()
//    val appConfigViewModel: AppConfigAndLimitViewModel by viewModels()  // ✅ ADD THIS
//
//
//    // ✅ NEW: Add time tracker
//    private lateinit var appTimeTracker: AppTimeTracker
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        appConfigViewModel.setCurrentAppVersion(this)
//
//
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        reviewManager = InAppReviewManager(this)
//        reviewManager.incrementLaunchCount()
//
//        billingManager = BillingManager(this, authViewModel)
//
//
//        // ✅ NEW: Initialize and start time tracker
//        appTimeTracker = AppTimeTracker(categoriesViewModel)
//        appTimeTracker.start()
//
//        // ✅ OPTIMIZATION: Trigger config load BEFORE setContent
//        lifecycleScope.launch {
//            appConfigViewModel.refreshAppConfiguration()
//            delay(100)  // Wait for initial config load
//        }
//
//        enableEdgeToEdge()
//        setContent {
//            PromptTubeAiTheme {
//                val navController = rememberNavController()
//                val context = LocalContext.current  // ✅ Get context
//
//                // ✅ Collect app config states
//                val isMaintenanceMode by appConfigViewModel.isMaintenanceMode.collectAsState()
//                val forceUpdateRequired by appConfigViewModel.forceUpdateRequired.collectAsState()
//                val currentVersion by appConfigViewModel.currentAppVersion.collectAsState()
//                val latestVersion by appConfigViewModel.latestVersion.collectAsState()
//                val updateMessage by appConfigViewModel.updateMessage.collectAsState()
//                val isLoading by appConfigViewModel.isLoading.collectAsState()
//
//                LaunchedEffect(Unit) {
//
//                    // ✅ Set application context first
//                    categoriesViewModel.setApplicationContext(context)
//                    categoriesViewModel.setContext(context) // ✅ Initialize ImagePrefetcher
//
//                    delay(500)
//
//                    // ✅ Verify subscriptions
//                    verifySubscriptionsOnLaunch()
//
//                    // ✅ Initialize daily usage
//                    val currentUser = authViewModel.getCurrentUser()
//                    if (currentUser != null) {
//                        categoriesViewModel.initializeDailyUsage(currentUser.uid)
//                    }
//
//                    // ✅ TRIGGER HOME PROMPTS LOAD (includes automatic prefetching)
//                    categoriesViewModel.loadHomePrompts(context, forceRefresh = false)
//                }
//
//                // ✅ CHECK: Show maintenance or update screen if needed
//                when {
//                    isLoading -> {
//                        // ✅ UPDATED: Use custom LoadingIndicator instead of CircularProgressIndicator
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(
//                                    brush = Brush.radialGradient(
//                                        colors = listOf(
//                                            GrowthGreenPrimary.copy(alpha = 0.15f),
//                                            GrowthGreenBackground,
//                                            GrowthGreenSecondary.copy(alpha = 0.1f),
//                                            GrowthGreenBackground
//                                        ),
//                                        radius = 1200f
//                                    )
//                                ),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            LoadingIndicator(
//                                message = "",  // ✅ No message
//                                orbSize = 80.dp,
//                                modifier = Modifier
//                            )
//                        }
//                    }
//
//                    isMaintenanceMode -> {
//                        // Show maintenance screen
//                        MaintenanceScreen(
//                            onRetry = {
//                                appConfigViewModel.refreshAppConfiguration()
//                            }
//                        )
//                    }
//
//                    forceUpdateRequired && appConfigViewModel.isUpdateRequired() -> {
//                        // Show force update screen
//                        ForceUpdateScreen(
//                            currentVersion = currentVersion,
//                            latestVersion = latestVersion,
//                            updateMessage = updateMessage
//                        )
//                    }
//
//                    else -> {
//                        // ✅ Normal app flow
//                        NavGraph(navController = navController)
//                    }
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            delay(3000)
//            if (reviewManager.shouldShowReview()) {
//                reviewManager.requestReview()
//            }
//        }
//
//        // ✅ Periodic verification
//        lifecycleScope.launch {
//            while (true) {
//                delay(600000) // Every 10 minutes
//                verifySubscriptions()
//            }
//        }
//    }
//
//
//    private fun verifySubscriptionsOnLaunch() {
//        lifecycleScope.launch {
//            Log.d("MainActivity", "🔄 Verifying subscriptions on launch...")
//
//            // Check if there are unacknowledged purchases
//            val params = QueryPurchasesParams.newBuilder()
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build()
//
//            billingManager.billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
//                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                    purchases?.forEach { purchase ->
//                        Log.d("MainActivity", "Found purchase: ${purchase.purchaseToken}, acknowledged=${purchase.isAcknowledged}")
//
//                        if (!purchase.isAcknowledged) {
//                            Log.d("MainActivity", "⚠️ Unacknowledged purchase found - saving and acknowledging")
//                            billingManager.handlePurchase(purchase)
//                        }
//                    }
//                }
//            }
//
//            // Full verification
//            billingManager.verifyActiveSubscriptions { hasActive ->
//                if (hasActive) {
//                    Log.d("MainActivity", "✅ Premium verified")
//                } else {
//                    Log.d("MainActivity", "❌ No active premium")
//                }
//            }
//        }
//    }
//
//    private fun verifySubscriptions() {
//        lifecycleScope.launch {
//            Log.d("MainActivity", "🔄 Verifying subscriptions...")
//
//            // ✅ Clear cache to get fresh data
//            SubscriptionUtils.clearAllCache()
//
//            billingManager.verifyActiveSubscriptions { hasActive ->
//                if (hasActive) {
//                    Log.d("MainActivity", "✅ Premium access granted")
//                } else {
//                    Log.d("MainActivity", "❌ No active subscription")
//
//                    // ✅ Check if user was premium before (possible refund)
//                    val currentUser = authViewModel.currentUser.value
//                    if (currentUser?.isPremium == true) {
//                        Log.w("MainActivity", "⚠️ Possible refund detected - user was premium")
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        billingManager.endBillingConnection()
//    }
//}


// ═══════════════════════════════════════════════════════════════════════════
// SKELETON LOADING CONTENT (PIXEL-PERFECT MATCH FOR HOME SCREEN)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
private fun SkeletonHomeContent(innerPadding: PaddingValues) {

    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthGreenBackground),
        contentPadding = PaddingValues(
            top = innerPadding.calculateTopPadding() + 12.dp,
            bottom = innerPadding.calculateBottomPadding() + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // ✅ Show 3 sections (Recent, Most Viewed, Trending)
        repeat(3) {
            item { SkeletonSectionHeader() }
            item { SkeletonPromptsRow() }
        }
    }
}

// ✅ PIXEL-PERFECT SECTION HEADER SKELETON
@Composable
private fun SkeletonSectionHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),  // ✅ Same as PromptSectionHeader
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)  // ✅ Same spacing
    ) {
        // ✅ Icon box shimmer
        SectionIconShimmer()

        // ✅ Text lines shimmer
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {  // ✅ Same as PromptSectionHeader
            SectionTitleShimmer()
            SectionSubtitleShimmer()
        }
    }
}

@Composable
private fun SectionIconShimmer() {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)
    val transition = rememberInfiniteTransition(label = "iconShimmer")

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

    val shimmerColors = listOf(
        GrowthGreenPrimary.copy(alpha = 0.08f),
        GrowthGreenPrimary.copy(alpha = pulseAnim),
        GrowthGreenPrimary.copy(alpha = 0.08f)
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

    Surface(
        color = GrowthGreenPrimary.copy(alpha = 0.12f),  // ✅ Same as PromptSectionHeader
        shape = RoundedCornerShape(14.dp),  // ✅ Same radius
        modifier = Modifier.size(52.dp)  // ✅ Same size
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.sweepGradient(
                        colors = shimmerColors,
                        center = Offset(26f, 26f)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Empty - just the shimmer effect
        }
    }
}

@Composable
private fun SectionTitleShimmer() {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)
    val transition = rememberInfiniteTransition(label = "titleShimmer")
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
        GrowthGreenSurface.copy(alpha = 0.9f),
        GrowthGreenSurface.copy(alpha = 0.3f),
        GrowthGreenSurface.copy(alpha = 0.9f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 200f, 0f)
    )

    Box(
        modifier = Modifier
            .width(140.dp)  // ✅ Matches typical title width
            .height(20.dp)  // ✅ Matches title font size (19.sp ≈ 20dp)
            .clip(RoundedCornerShape(4.dp))
            .background(brush)
    )
}

@Composable
private fun SectionSubtitleShimmer() {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)
    val transition = rememberInfiniteTransition(label = "subtitleShimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                delayMillis = 200,  // ✅ Staggered animation
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val shimmerColors = listOf(
        GrowthGreenSurface.copy(alpha = 0.9f),
        GrowthGreenSurface.copy(alpha = 0.3f),
        GrowthGreenSurface.copy(alpha = 0.9f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 200f, 0f)
    )

    Box(
        modifier = Modifier
            .width(100.dp)  // ✅ Matches typical subtitle width
            .height(14.dp)  // ✅ Matches subtitle font size (13.sp ≈ 14dp)
            .clip(RoundedCornerShape(4.dp))
            .background(brush)
    )
}

// ✅ PIXEL-PERFECT PROMPTS ROW SKELETON
@Composable
private fun SkeletonPromptsRow() {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),  // ✅ Same as PromptsLazyRow
        contentPadding = PaddingValues(horizontal = 20.dp)  // ✅ Same padding
    ) {
        items(6) {  // ✅ Show 6 skeleton cards
            SkeletonPromptCard()
        }
    }
}

// ✅ PIXEL-PERFECT PROMPT CARD SKELETON (matches PromptCard exactly)
@Composable
private fun SkeletonPromptCard() {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)
    Card(
        modifier = Modifier
            .width(120.dp)  // ✅ Same width as PromptCard wrapper
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),  // ✅ Same radius
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),  // ✅ Same elevation
        colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ✅ EXACT MATCH: Image area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)  // ✅ Same aspect ratio
                    .padding(8.dp)  // ✅ Same padding
            ) {
                // ✅ EXACT MATCH: Inner Card
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(12.dp),  // ✅ Same radius
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),  // ✅ Same elevation
                    colors = CardDefaults.cardColors(containerColor = GrowthGreenSurface)
                ) {
                    // ✅ Premium image shimmer
                    PremiumCardImageShimmer()
                }
            }

            // ✅ EXACT MATCH: Text area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)  // ✅ Same height as PromptCard text area
                    .padding(horizontal = 10.dp, vertical = 4.dp),  // ✅ Same padding
                contentAlignment = Alignment.Center
            ) {
                // ✅ Text shimmer (single line, centered)
                CardTextShimmer()
            }
        }
    }
}

// ✅ Premium image shimmer for prompt cards (diagonal sweep + glow)
@Composable
private fun PremiumCardImageShimmer() {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)
    val transition = rememberInfiniteTransition(label = "cardImageShimmer")

    // Diagonal sweep animation
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

    // Pulsing brightness
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
        Color.White.copy(alpha = pulseAnim * 1.3f),  // Bright highlight
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
            .background(GrowthGreenSurface.copy(alpha = 0.4f))  // Base layer
            .background(brush)  // Shimmer layer
    )
}

// ✅ Text shimmer for prompt card title
@Composable
private fun CardTextShimmer() {
    val GrowthGreenSurface = Color(0xFFFFFFFF)
    val GrowthGreenBackground = Color(0xFFF1F8E9)
    val GrowthGreenPrimary = Color(0xFF4CAF50)
    val GrowthGreenSecondary = Color(0xFF81C784)
    val GrowthGreenTertiary = Color(0xFF388E3C)
    val transition = rememberInfiniteTransition(label = "cardTextShimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -150f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1300,
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
            .fillMaxWidth(0.8f)  // ✅ 80% width for typical title length
            .height(14.dp)  // ✅ Matches text line height
            .clip(RoundedCornerShape(4.dp))
            .background(GrowthGreenSurface.copy(alpha = 0.4f))  // Base
            .background(brush)  // Shimmer
    )
}
