//package com.vimla.myapplication.Screen.SpaceMaintenance
//
//import androidx.compose.animation.core.LinearEasing
//import androidx.compose.animation.core.RepeatMode
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.CornerRadius
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.BlendMode
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.drawscope.rotate
//
//@Composable
//fun MaintenanceScene(innerPadding: PaddingValues) {
//    val transition = rememberInfiniteTransition(label = "eva_transition")
//
//    val astronautFloat by transition.animateFloat(
//        initialValue = -4f,
//        targetValue = 5f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(4200, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "astronaut_float"
//    )
//
//    val tetherSway by transition.animateFloat(
//        initialValue = -1f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(5200, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "tether_sway"
//    )
//
//    val pulse by transition.animateFloat(
//        initialValue = 0.45f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1600, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "tool_pulse"
//    )
//
//    val rocket = Rocket()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF01040A))
//            .padding(innerPadding)
//    ) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            drawRect(Color(0xFF01040A))
//
//            drawBackgroundStars()
//            drawEarthBody()
//            with(rocket) { draw() }
//            drawAstronaut(astronautFloat, pulse)
//            drawTether(tetherSway)
//            drawToolGlow(astronautFloat, pulse)
//        }
//    }
//}
//
//private fun DrawScope.drawBackgroundStars() {
//    val stars = listOf(
//        Offset(0.09f, 0.10f), Offset(0.14f, 0.06f), Offset(0.19f, 0.14f),
//        Offset(0.26f, 0.08f), Offset(0.35f, 0.11f), Offset(0.49f, 0.07f),
//        Offset(0.58f, 0.10f), Offset(0.67f, 0.13f), Offset(0.79f, 0.09f),
//        Offset(0.87f, 0.15f), Offset(0.17f, 0.22f), Offset(0.29f, 0.19f),
//        Offset(0.43f, 0.21f), Offset(0.55f, 0.18f), Offset(0.71f, 0.23f)
//    )
//
//    stars.forEachIndexed { i, p ->
//        drawCircle(
//            color = if (i % 4 == 0) Color(0xFFE6F2FF) else Color.White.copy(alpha = 0.85f),
//            radius = size.minDimension * (0.0015f + (i % 3) * 0.0008f),
//            center = Offset(size.width * p.x, size.height * p.y)
//        )
//    }
//}
//
//private fun DrawScope.drawEarthBody() {
//    val center = Offset(size.width * 0.15f, size.height * 0.08f)
//    val radius = size.width * 0.42f
//
//    drawCircle(
//        brush = Brush.radialGradient(
//            colors = listOf(
//                Color(0xFFF6FBFF),
//                Color(0xFFBEDBFF),
//                Color(0xFF5F9FEA),
//                Color(0xFF0D2345),
//                Color(0xFF04101F),
//                Color.Transparent
//            ),
//            center = Offset(size.width * 0.06f, size.height * 0.02f),
//            radius = radius * 1.05f
//        ),
//        radius = radius,
//        center = center
//    )
//
//    drawCircle(
//        brush = Brush.radialGradient(
//            colors = listOf(
//                Color.White.copy(alpha = 0.95f),
//                Color(0xFFDDEEFF).copy(alpha = 0.38f),
//                Color.Transparent
//            ),
//            center = Offset(size.width * 0.02f, size.height * 0.0f),
//            radius = radius * 0.7f
//        ),
//        radius = radius * 0.95f,
//        center = center,
//        blendMode = BlendMode.Screen
//    )
//
//    drawCircle(
//        color = Color.White.copy(alpha = 0.18f),
//        radius = radius,
//        center = center,
//        style = Stroke(width = size.minDimension * 0.006f)
//    )
//}
//
//private fun DrawScope.drawAstronaut(floatY: Float, pulse: Float) {
//    val x = size.width * 0.41f
//    val y = size.height * 0.50f + floatY
//    val u = size.minDimension * 0.0037f
//
//    rotate(degrees = -25f, pivot = Offset(x, y)) {
//        drawRoundRect(
//            brush = Brush.linearGradient(
//                colors = listOf(
//                    Color(0xFFD5DDE4),
//                    Color(0xFFB4BEC8),
//                    Color(0xFF98A5B1)
//                )
//            ),
//            topLeft = Offset(x - 33f * u, y - 10f * u),
//            size = Size(20f * u, 28f * u),
//            cornerRadius = CornerRadius(4f * u, 4f * u)
//        )
//
//        val torso = Path().apply {
//            moveTo(x - 16f * u, y - 12f * u)
//            lineTo(x + 8f * u, y - 14f * u)
//            lineTo(x + 15f * u, y + 8f * u)
//            lineTo(x - 4f * u, y + 20f * u)
//            lineTo(x - 20f * u, y + 7f * u)
//            close()
//        }
//        drawPath(
//            path = torso,
//            brush = Brush.linearGradient(
//                colors = listOf(
//                    Color(0xFFF7F9FB),
//                    Color(0xFFD8E0E7),
//                    Color(0xFFBCC7D1)
//                ),
//                start = Offset(x - 20f * u, y - 16f * u),
//                end = Offset(x + 16f * u, y + 20f * u)
//            )
//        )
//
//        drawCircle(
//            color = Color(0xFFF8FBFD),
//            radius = 11f * u,
//            center = Offset(x + 16f * u, y - 10f * u)
//        )
//
//        drawCircle(
//            brush = Brush.linearGradient(
//                colors = listOf(
//                    Color(0xFFF2FAFF),
//                    Color(0xFFA8CBE8),
//                    Color(0xFF567A98)
//                ),
//                start = Offset(x + 8f * u, y - 20f * u),
//                end = Offset(x + 24f * u, y - 2f * u)
//            ),
//            radius = 7.2f * u,
//            center = Offset(x + 16.5f * u, y - 10f * u)
//        )
//
//        drawLine(
//            color = Color(0xFFE2E8EE),
//            start = Offset(x - 2f * u, y - 3f * u),
//            end = Offset(x + 37f * u, y - 2f * u),
//            strokeWidth = 6f * u,
//            cap = StrokeCap.Round
//        )
//
//        drawLine(
//            color = Color(0xFFD6DEE6),
//            start = Offset(x - 10f * u, y + 2f * u),
//            end = Offset(x - 27f * u, y + 18f * u),
//            strokeWidth = 6f * u,
//            cap = StrokeCap.Round
//        )
//
//        drawLine(
//            color = Color(0xFFD6DEE6),
//            start = Offset(x - 2f * u, y + 16f * u),
//            end = Offset(x - 20f * u, y + 49f * u),
//            strokeWidth = 6.3f * u,
//            cap = StrokeCap.Round
//        )
//
//        drawLine(
//            color = Color(0xFFD6DEE6),
//            start = Offset(x + 11f * u, y + 14f * u),
//            end = Offset(x + 33f * u, y + 36f * u),
//            strokeWidth = 6.3f * u,
//            cap = StrokeCap.Round
//        )
//
//        drawLine(
//            color = Color(0xFF7D8792),
//            start = Offset(x - 20f * u, y + 49f * u),
//            end = Offset(x - 27f * u, y + 54f * u),
//            strokeWidth = 4f * u,
//            cap = StrokeCap.Round
//        )
//
//        drawLine(
//            color = Color(0xFF7D8792),
//            start = Offset(x + 33f * u, y + 36f * u),
//            end = Offset(x + 40f * u, y + 36f * u),
//            strokeWidth = 4f * u,
//            cap = StrokeCap.Round
//        )
//
//        val hand = Offset(x + 37f * u, y - 2f * u)
//
//        drawCircle(
//            color = Color.White,
//            radius = 3.1f * u,
//            center = hand
//        )
//
//        drawCircle(
//            brush = Brush.radialGradient(
//                colors = listOf(
//                    Color.White.copy(alpha = 0.90f * pulse),
//                    Color(0xFFB8D9F5).copy(alpha = 0.48f * pulse),
//                    Color.Transparent
//                ),
//                center = hand,
//                radius = 18f * u
//            ),
//            radius = 18f * u,
//            center = hand,
//            blendMode = BlendMode.Screen
//        )
//    }
//}
//
//private fun DrawScope.drawTether(sway: Float) {
//    val p = Path().apply {
//        moveTo(size.width * 0.23f, size.height * 0.58f)
//        cubicTo(
//            size.width * 0.29f, size.height * 0.56f + sway,
//            size.width * 0.33f, size.height * 0.54f - sway,
//            size.width * 0.36f, size.height * 0.52f
//        )
//        cubicTo(
//            size.width * 0.40f, size.height * 0.49f,
//            size.width * 0.42f, size.height * 0.50f,
//            size.width * 0.44f, size.height * 0.50f
//        )
//    }
//
//    drawPath(
//        path = p,
//        color = Color(0x99C5D0DC),
//        style = Stroke(width = size.minDimension * 0.0026f, cap = StrokeCap.Round)
//    )
//}
//
//private fun DrawScope.drawToolGlow(floatY: Float, pulse: Float) {
//    val center = Offset(size.width * 0.532f, size.height * 0.494f + floatY)
//
//    drawCircle(
//        brush = Brush.radialGradient(
//            colors = listOf(
//                Color.White.copy(alpha = 0.42f * pulse),
//                Color(0xFFB5D9FF).copy(alpha = 0.25f * pulse),
//                Color.Transparent
//            ),
//            center = center,
//            radius = size.minDimension * 0.065f
//        ),
//        radius = size.minDimension * 0.065f,
//        center = center,
//        blendMode = BlendMode.Screen
//    )
//}