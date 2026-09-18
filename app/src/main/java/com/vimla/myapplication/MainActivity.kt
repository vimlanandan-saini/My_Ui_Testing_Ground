package com.vimla.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.razorpay.Checkout
import com.vimla.myapplication.Screen.ProductCardTestScreen
import com.vimla.myapplication.ui.theme.JadeFreshBackground
import com.vimla.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Checkout.preload(applicationContext)

        // Force light (white) status bar icons — the header is a solid dark green,
        // so the default auto style (which can pick dark icons) was blending in.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            MyApplicationTheme {
                val context = LocalContext.current

                // ── UI BENCH ─────────────────────────────────────────────────
                // The Product Card bench replaces the promo screen while we
                // iterate on the card design. To get the old screen back, swap
                // the ProductCardTestScreen call below for:
                //
                //     PromotionVideoMakingScreen(onOrderNowClick = { /* … */ })
                //
                // (and re-add its import). Nothing else in this file needs to
                // change — Razorpay's preload and the edge-to-edge setup above
                // are untouched.
                //
                // enableEdgeToEdge draws behind the system bars, so the bench
                // takes the status-bar inset as padding. Without it the bench
                // header sits under the clock, and every judgement you make
                // about the card's top spacing would be off by ~24dp.
                //
                // The Surface fills the whole window and the inset goes on the
                // Box INSIDE it, not on the Surface — otherwise the jade
                // background stops at the status bar and a white strip shows
                // through above it.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = JadeFreshBackground
                ) {
                    Box(
                        modifier = Modifier.padding(
                            WindowInsets.statusBars.asPaddingValues()
                        )
                    ) {
                        ProductCardTestScreen(
                            onNotify = { message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}
