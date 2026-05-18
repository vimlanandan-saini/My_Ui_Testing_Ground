package com.vimla.myapplication.Screen.ImageCompressor

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.zoomable(
    minScale: Float = 1f,
    maxScale: Float = 5f
): Modifier = composed {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    this
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = { tapOffset ->
                    if (scale > 1f) {
                        scale = 1f
                        offset = Offset.Zero
                    } else {
                        scale = 2.5f
                        val targetOffset = Offset(
                            x = (size.width / 2 - tapOffset.x) * scale,
                            y = (size.height / 2 - tapOffset.y) * scale
                        )
                        offset = targetOffset
                    }
                }
            )
        }
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                scale = (scale * zoom).coerceIn(minScale, maxScale)

                if (scale > 1f) {
                    val maxX = (size.width * (scale - 1)) / 2
                    val maxY = (size.height * (scale - 1)) / 2

                    offset = Offset(
                        x = (offset.x + pan.x).coerceIn(-maxX, maxX),
                        y = (offset.y + pan.y).coerceIn(-maxY, maxY)
                    )
                } else {
                    offset = Offset.Zero
                }
            }
        }
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
        )
}
