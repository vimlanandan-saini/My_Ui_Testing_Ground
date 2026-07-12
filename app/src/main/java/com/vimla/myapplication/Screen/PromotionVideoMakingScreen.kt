package com.vimla.myapplication.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vimla.myapplication.R
import com.vimla.myapplication.ui.theme.MyApplicationTheme


// Primary Colors
val JadeFreshPrimary          = Color(0xFF2D8563)   // Deeper mint jade — identity color
val JadeFreshSecondary        = Color(0xFF4FB793)   // Rich aquamint — supporting color
val JadeFreshTertiary         = Color(0xFF1F6046)   // Deep jade — structure & depth

// Background Colors
val JadeFreshBackground       = Color(0xFFE0F3EE)   // Minty cool surface — main screen bg
val JadeFreshSurface          = Color(0xFFFFFFFF)   // Pure white — cards, dialogs
val JadeFreshSurfaceVariant   = Color(0xFFC6E4D8)   // Deeper mint card — search, chips

// Text Colors
val JadeFreshOnPrimary        = Color(0xFFFFFFFF)   // On buttons, FAB, nav active
val JadeFreshOnSecondary      = Color(0xFFFFFFFF)   // On secondary components
val JadeFreshOnBackground     = Color(0xFF0A211B)   // Jade-black — main body text
val JadeFreshOnSurface        = Color(0xFF0A211B)   // On white cards
val JadeFreshOnSurfaceVariant = Color(0xFF295C47)   // On tinted surfaces, hints

// Accent & Special Colors
val JadeFreshAccent           = Color(0xFFD18627)   // Saffron gold — CTAs, "Add" buttons, offers
val JadeFreshPremium          = Color(0xFFE27F35)   // Mango orange — premium badges, highlights
val JadeFreshSuccess          = Color(0xFF2D8563)   // Same as primary — order placed, fresh tag
val JadeFreshError            = Color(0xFFC43030)   // Deep red — errors, out of stock
val JadeFreshWarning          = Color(0xFFF1933C)   // Amber — low stock, expiry notices


// NOTE: rename these to match your actual drawable file names if different
// (e.g. R.drawable.promo_1 ... promo_10).
private val promoImages = listOf(
    R.drawable.one,
    R.drawable.two,
    R.drawable.three,
    R.drawable.four,
    R.drawable.five,
    R.drawable.six,
    R.drawable.seven,
    R.drawable.eight,
    R.drawable.nine,
    R.drawable.ten,
)

@Composable
fun PromotionVideoMakingScreen(
    onOrderNowClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JadeFreshBackground)
    ) {
        AppHeader()

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            PromoImagePager(
                images = promoImages,
                modifier = Modifier.fillMaxWidth()
            )
        }

        OrderNowButton(
            onClick = onOrderNowClick,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        )
    }
}

@Composable
private fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = JadeFreshPrimary,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
            )
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(JadeFreshSurface),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.vindusha),
                contentDescription = "Vindusha logo",
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = "Vindusha",
                color = JadeFreshOnPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Your Village's Own Shop",
                color = JadeFreshOnPrimary.copy(alpha = 0.85f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun PromoImagePager(
    images: List<Int>,
    modifier: Modifier = Modifier
) {
    // pageCount as a lambda keeps the pager stable if the list ever changes size.
    val pagerState = rememberPagerState(pageCount = { images.size })

    // No page indicator by design — swipe only.
    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(JadeFreshSurface)
    ) { page ->
        Image(
            painter = painterResource(id = images[page]),
            contentDescription = "Promotion image ${page + 1}",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun OrderNowButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(58.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = JadeFreshAccent,
            contentColor = JadeFreshOnBackground
        )
    ) {
        Text(
            text = "ORDER NOW",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PromotionVideoMakingScreenPreview() {
    MyApplicationTheme {
        PromotionVideoMakingScreen()
    }
}