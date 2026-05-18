package com.vimla.myapplication.Screen

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vimla.myapplication.ui.theme.GrowthGreenPrimary
import com.vimla.myapplication.ui.theme.GrowthGreenSecondary
import kotlinx.coroutines.launch
import java.io.OutputStream
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun DodoBirdScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    var isCapturing by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Dodo Bird App Icon Generator",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Preview with gray background to show transparency
        Box(
            modifier = Modifier
                .size(512.dp)
                .background(Color(0xFFEEEEEE)),
            contentAlignment = Alignment.Center
        ) {
            // Icon capture layer
            Box(
                modifier = Modifier
                    .size(512.dp)
                    .drawWithContent {
                        graphicsLayer.record {
                            this@drawWithContent.drawContent()
                        }
                        drawLayer(graphicsLayer)
                    },
                contentAlignment = Alignment.Center
            ) {
                DodoBirdAppIcon(size = 512.dp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                scope.launch {
                    isCapturing = true
                    try {
                        val imageBitmap = graphicsLayer.toImageBitmap()
                        val bitmap = imageBitmap.asAndroidBitmap()

                        val result = saveBitmapToGallery(
                            context = context,
                            bitmap = bitmap,
                            displayName = "DodoBirdAppIcon"
                        )

                        if (result != null) {
                            Toast.makeText(
                                context,
                                "✅ App icon saved to Pictures!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "❌ Failed to save image",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Error: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        e.printStackTrace()
                    } finally {
                        isCapturing = false
                    }
                }
            },
            enabled = !isCapturing,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            if (isCapturing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Saving...")
            } else {
                Text("💾 Save 512x512 Square Icon")
            }
        }

        Text(
            text = "Perfect square with transparent background\nReady for app launcher",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

suspend fun saveBitmapToGallery(
    context: Context,
    bitmap: Bitmap,
    displayName: String
): Uri? {
    return try {
        val timestamp = System.currentTimeMillis()
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${displayName}_${timestamp}.png")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val contentResolver = context.contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear()
                values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                contentResolver.update(it, values, null, null)
            }
        }

        uri
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Composable
fun DodoBirdAppIcon(
    modifier: Modifier = Modifier,
    size: Dp = 512.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        // Gradient background box - PERFECT SQUARE (no rounded corners)
        Box(
            modifier = Modifier
                .size(size)
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFF8BC34A), // Light green
                            Color(0xFF66BB6A), // Green
                            Color(0xFF4CAF50), // Dark green
                            Color(0xFF8BC34A)  // Back to light green
                        )
                    )
                )
        )

        // Inner gradient - PERFECT SQUARE
        Box(
            modifier = Modifier
                .size(size - 16.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFC8E6C9), // Very light green center
                            Color(0xFFA5D6A7), // Light green
                            Color(0xFF81C784), // Medium green
                            Color(0xFF66BB6A)  // Darker green
                        )
                    )
                )
        )

        // Energy swirls (orange lightning bolts)
        androidx.compose.foundation.Canvas(
            modifier = Modifier.size(size)
        ) {
            drawEnergySwirls(this, 1f)
        }

        // Main Dodo Bird
        androidx.compose.foundation.Canvas(
            modifier = Modifier.size(size - 60.dp)
        ) {
            drawDodoBird(this)
        }
    }
}





/**
 * Professional Dodo Bird Logo - Matches your reference images
 * Rounded, chubby, minimalist style
 */
@Composable
fun DodoBirdLogo(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    showGlow: Boolean = true,
    showEnergySwirls: Boolean = true,
    glowIntensity: Float = 1f
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        // Outer glow effect
        if (showGlow) {
            Box(
                modifier = Modifier
                    .size(size + 60.dp)
                    .blur(radius = 30.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GrowthGreenPrimary.copy(alpha = glowIntensity * 0.5f),
                                GrowthGreenSecondary.copy(alpha = glowIntensity * 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        // Gradient background box
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(size / 4))
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFF8BC34A), // Light green
                            Color(0xFF66BB6A), // Green
                            Color(0xFF4CAF50), // Dark green
                            Color(0xFF8BC34A)  // Back to light green
                        )
                    )
                )
        )

        // Inner gradient circles (concentric rings)
        Box(
            modifier = Modifier
                .size(size - 8.dp)
                .clip(RoundedCornerShape(size / 4 - 4.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFC8E6C9), // Very light green center
                            Color(0xFFA5D6A7), // Light green
                            Color(0xFF81C784), // Medium green
                            Color(0xFF66BB6A)  // Darker green
                        )
                    )
                )
        )

        // Energy swirls (orange lightning bolts)
        if (showEnergySwirls) {
            androidx.compose.foundation.Canvas(
                modifier = Modifier.size(size)
            ) {
                drawEnergySwirls(this, glowIntensity)
            }
        }

        // Main Dodo Bird
        androidx.compose.foundation.Canvas(
            modifier = Modifier.size(size - 30.dp)
        ) {
            drawDodoBird(this)
        }
    }
}

/**
 * Draw the cute, chubby Dodo bird
 */
private fun drawDodoBird(drawScope: DrawScope) {
    with(drawScope) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val scale = size.width / 140f

        // === BODY (large rounded shape) ===
        val bodyPath = Path().apply {
            addOval(
                Rect(
                    left = centerX - 45f * scale,
                    top = centerY - 15f * scale,
                    right = centerX + 45f * scale,
                    bottom = centerY + 55f * scale
                )
            )
        }
        // White body with slight gradient
        drawPath(
            bodyPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFF5F5F5), // Off-white
                    Color(0xFFEEEEEE)  // Slightly darker
                )
            )
        )
        // Body outline
        drawPath(
            bodyPath,
            color = Color(0xFF66BB6A),
            style = Stroke(width = 2f * scale)
        )

        // === HEAD (rounded circle) ===
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFAFAFA),
                    Color(0xFFF0F0F0)
                )
            ),
            radius = 28f * scale,
            center = Offset(centerX - 8f * scale, centerY - 25f * scale)
        )
        // Head outline
        drawCircle(
            color = Color(0xFF66BB6A),
            radius = 28f * scale,
            center = Offset(centerX - 8f * scale, centerY - 25f * scale),
            style = Stroke(width = 2f * scale)
        )

        // === EYE ===
        // Eye white
        drawCircle(
            color = Color.White,
            radius = 8f * scale,
            center = Offset(centerX + 3f * scale, centerY - 28f * scale)
        )
        // Pupil (green)
        drawCircle(
            color = Color(0xFF4CAF50),
            radius = 4f * scale,
            center = Offset(centerX + 4f * scale, centerY - 27f * scale)
        )
        // Shine/highlight
        drawCircle(
            color = Color.White,
            radius = 1.5f * scale,
            center = Offset(centerX + 5.5f * scale, centerY - 28.5f * scale)
        )
        // Eye outline
        drawCircle(
            color = Color(0xFF66BB6A),
            radius = 8f * scale,
            center = Offset(centerX + 3f * scale, centerY - 28f * scale),
            style = Stroke(width = 1.5f * scale)
        )

        // === BEAK (curved hook shape) ===
        val beakPath = Path().apply {
            moveTo(centerX + 18f * scale, centerY - 25f * scale)
            quadraticBezierTo(
                centerX + 32f * scale, centerY - 28f * scale,
                centerX + 35f * scale, centerY - 20f * scale
            )
            quadraticBezierTo(
                centerX + 32f * scale, centerY - 18f * scale,
                centerX + 20f * scale, centerY - 18f * scale
            )
            close()
        }
        // Beak fill (green)
        drawPath(beakPath, color = Color(0xFF66BB6A))
        // Beak outline
        drawPath(
            beakPath,
            color = Color(0xFF4CAF50),
            style = Stroke(width = 1.5f * scale)
        )

        // === WINGS (small rounded shapes on sides) ===
        // Left wing
        val leftWingPath = Path().apply {
            addOval(
                Rect(
                    left = centerX - 55f * scale,
                    top = centerY + 5f * scale,
                    right = centerX - 30f * scale,
                    bottom = centerY + 25f * scale
                )
            )
        }
        drawPath(leftWingPath, color = Color(0xFFE0F2F1))
        drawPath(
            leftWingPath,
            color = Color(0xFF66BB6A),
            style = Stroke(width = 1.5f * scale)
        )
        // Wing detail lines
        for (i in 0..2) {
            drawLine(
                color = Color(0xFF81C784).copy(alpha = 0.5f),
                start = Offset(centerX - 50f * scale, centerY + (10f + i * 5f) * scale),
                end = Offset(centerX - 35f * scale, centerY + (10f + i * 5f) * scale),
                strokeWidth = 1f * scale
            )
        }

        // Right wing
        val rightWingPath = Path().apply {
            addOval(
                Rect(
                    left = centerX + 30f * scale,
                    top = centerY + 5f * scale,
                    right = centerX + 55f * scale,
                    bottom = centerY + 25f * scale
                )
            )
        }
        drawPath(rightWingPath, color = Color(0xFFC8E6C9))
        drawPath(
            rightWingPath,
            color = Color(0xFF66BB6A),
            style = Stroke(width = 1.5f * scale)
        )
        // Wing detail lines
        for (i in 0..2) {
            drawLine(
                color = Color(0xFF81C784).copy(alpha = 0.5f),
                start = Offset(centerX + 35f * scale, centerY + (10f + i * 5f) * scale),
                end = Offset(centerX + 50f * scale, centerY + (10f + i * 5f) * scale),
                strokeWidth = 1f * scale
            )
        }

        // === LEGS (short stubby legs) ===
        // Left leg
        drawLine(
            color = Color(0xFF81C784),
            start = Offset(centerX - 15f * scale, centerY + 50f * scale),
            end = Offset(centerX - 15f * scale, centerY + 62f * scale),
            strokeWidth = 3f * scale
        )
        // Left foot
        drawLine(
            color = Color(0xFF81C784),
            start = Offset(centerX - 20f * scale, centerY + 62f * scale),
            end = Offset(centerX - 10f * scale, centerY + 62f * scale),
            strokeWidth = 2f * scale
        )

        // Right leg
        drawLine(
            color = Color(0xFF81C784),
            start = Offset(centerX + 15f * scale, centerY + 50f * scale),
            end = Offset(centerX + 15f * scale, centerY + 62f * scale),
            strokeWidth = 3f * scale
        )
        // Right foot
        drawLine(
            color = Color(0xFF81C784),
            start = Offset(centerX + 10f * scale, centerY + 62f * scale),
            end = Offset(centerX + 20f * scale, centerY + 62f * scale),
            strokeWidth = 2f * scale
        )

        // === TAIL (small rounded feathers at back) ===
        for (i in 0..2) {
            val angle = (i - 1) * 20f
            val radians = Math.toRadians(angle.toDouble())
            val tailX = centerX + 40f * scale + cos(radians).toFloat() * 8f * scale
            val tailY = centerY + 30f * scale + sin(radians).toFloat() * 5f * scale

            drawCircle(
                color = Color(0xFFA5D6A7),
                radius = 5f * scale,
                center = Offset(tailX, tailY)
            )
            drawCircle(
                color = Color(0xFF66BB6A),
                radius = 5f * scale,
                center = Offset(tailX, tailY),
                style = Stroke(width = 1f * scale)
            )
        }
    }
}

/**
 * Draw energy swirls (lightning bolts and circular rings)
 */
private fun drawEnergySwirls(drawScope: DrawScope, intensity: Float) {
    with(drawScope) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val scale = size.width / 200f

        // Orange lightning bolts
        val lightningPositions = listOf(
            Pair(Offset(centerX - 60f * scale, centerY - 40f * scale), Offset(centerX - 50f * scale, centerY - 20f * scale)),
            Pair(Offset(centerX + 55f * scale, centerY - 35f * scale), Offset(centerX + 65f * scale, centerY - 15f * scale)),
            Pair(Offset(centerX - 65f * scale, centerY + 20f * scale), Offset(centerX - 55f * scale, centerY + 40f * scale)),
            Pair(Offset(centerX + 60f * scale, centerY + 25f * scale), Offset(centerX + 70f * scale, centerY + 45f * scale)),
            Pair(Offset(centerX + 10f * scale, centerY - 75f * scale), Offset(centerX + 15f * scale, centerY - 55f * scale)),
            Pair(Offset(centerX - 15f * scale, centerY + 60f * scale), Offset(centerX - 10f * scale, centerY + 75f * scale))
        )

        lightningPositions.forEach { (start, end) ->
            // Glow effect
            drawLine(
                color = Color(0xFFFF9800).copy(alpha = 0.3f * intensity),
                start = start,
                end = end,
                strokeWidth = 4f * scale
            )
            // Bright center
            drawLine(
                color = Color(0xFFFF9800).copy(alpha = 0.8f * intensity),
                start = start,
                end = end,
                strokeWidth = 2f * scale
            )
        }

        // Circular energy rings
        for (i in 0..2) {
            drawCircle(
                color = Color(0xFFFFB74D).copy(alpha = (0.2f - i * 0.05f) * intensity),
                radius = (35f + i * 25f) * scale,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2f * scale)
            )
        }

        // Small energy dots
        val dotPositions = listOf(
            Offset(centerX - 50f * scale, centerY - 50f * scale),
            Offset(centerX + 50f * scale, centerY - 45f * scale),
            Offset(centerX - 55f * scale, centerY + 10f * scale),
            Offset(centerX + 55f * scale, centerY + 15f * scale),
            Offset(centerX, centerY - 70f * scale)
        )

        dotPositions.forEach { pos ->
            drawCircle(
                color = Color(0xFFFFB74D).copy(alpha = 0.6f * intensity),
                radius = 3f * scale,
                center = pos
            )
        }
    }
}

/**
 * Small Dodo Logo - For toolbars
 */
@Composable
fun DodoBirdLogoSmall(modifier: Modifier = Modifier) {
    DodoBirdLogo(
        modifier = modifier,
        size = 48.dp,
        showGlow = false,
        showEnergySwirls = false
    )
}

/**
 * Large Dodo Logo - For splash screens
 */
@Composable
fun DodoBirdLogoLarge(modifier: Modifier = Modifier) {
    DodoBirdLogo(
        modifier = modifier,
        size = 200.dp,
        showGlow = true,
        showEnergySwirls = true,
        glowIntensity = 1f
    )
}
