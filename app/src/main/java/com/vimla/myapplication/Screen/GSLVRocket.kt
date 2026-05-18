package com.vimla.myapplication.Screen

import android.graphics.Paint as NativePaint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import kotlin.math.cos
import kotlin.math.sin

private val Bg = Color(0xFF090909)

private val WhiteEdge = Color(0xFFD1CCC2)
private val WhiteMid = Color(0xFFE8E3D9)
private val WhiteHi = Color(0xFFF6F3EB)
private val WhiteWarm = Color(0xFFF0EADF)
private val WhiteShadow = Color(0xFFBDB7AC)

private val LineDark = Color(0xFF5F5B56)
private val LineMid = Color(0xFF8D8880)
private val LineSoft = Color(0xFFC4BDB0)

private val RingLight = Color(0xFFD8CC9E)
private val RingDark = Color(0xFFB9AD77)

private val TrussDark = Color(0xFF3A3A3A)
private val TrussMid = Color(0xFF545454)
private val TrussLine = Color(0xFF8D8D8D)

// 3D Metallic Nozzle colours
private val NozzleMetalDark = Color(0xFF1A0E07)
private val NozzleMetalMid = Color(0xFF7A4A2D)
private val NozzleMetalHi = Color(0xFFDFAB81)

private val FlagSaffron = Color(0xFFFF9933)
private val FlagGreen = Color(0xFF138808)
private val FlagBlue = Color(0xFF000080)

private val IsroOrange = Color(0xFFE46D1B)

@Composable
fun GSLVRocket(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Bg)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w * 0.5f

            val rocketTop = h * 0.035f
            val rocketBottom = h * 0.985f
            val rocketH = rocketBottom - rocketTop

            val coreW = w * 0.212f
            val coreHW = coreW / 2f

            val boosterW = coreW * 0.80f
            val boosterHW = boosterW / 2f

            val fairW = coreW * 1.28f
            val fairHW = fairW / 2f

            val boosterGap = coreW * 0.028f
            val leftBoosterCx = cx - coreHW - boosterGap - boosterHW
            val rightBoosterCx = cx + coreHW + boosterGap + boosterHW

            val yFairTip = rocketTop
            val yFairOgiveEnd = rocketTop + rocketH * 0.105f
            val yFairCylEnd = rocketTop + rocketH * 0.182f
            val yFairShoulderEnd = rocketTop + rocketH * 0.208f

            val yEquipTop = yFairShoulderEnd
            val yEquipBottom = rocketTop + rocketH * 0.235f

            val yCryoTop = yEquipBottom
            val yCryoBottom = rocketTop + rocketH * 0.390f

            val yTrussTop = yCryoBottom
            val yTrussBottom = rocketTop + rocketH * 0.447f

            val yCoreTop = yTrussBottom
            val yCoreBottom = rocketTop + rocketH * 0.860f

            val yBoatTailBottom = rocketTop + rocketH * 0.924f
            val yCenterNozzleBottom = rocketTop + rocketH * 0.965f

            val yBoosterTip = rocketTop + rocketH * 0.345f
            val yBoosterNoseEnd = rocketTop + rocketH * 0.410f
            val yBoosterSkirtTop = rocketTop + rocketH * 0.900f
            val yBoosterBase = yCoreBottom
            val yBoosterNozzleBottom = rocketTop + rocketH * 0.923f

            drawAmbientShadows(cx, coreHW, yCoreTop, yCoreBottom)

            drawBooster(
                centerX = leftBoosterCx,
                halfW = boosterHW,
                tipY = yBoosterTip,
                noseEndY = yBoosterNoseEnd,
                skirtTopY = yBoosterSkirtTop,
                baseY = yBoosterBase,
                nozzleBottomY = yBoosterNozzleBottom,
                englishTokens = listOf("I", "S", "R", "O"),
                hindiTokens = listOf("इ", "स", "रो"),
                seamOnInnerSide = true
            )

            drawBooster(
                centerX = rightBoosterCx,
                halfW = boosterHW,
                tipY = yBoosterTip,
                noseEndY = yBoosterNoseEnd,
                skirtTopY = yBoosterSkirtTop,
                baseY = yBoosterBase,
                nozzleBottomY = yBoosterNozzleBottom,
                englishTokens = listOf("I", "N", "D", "I", "A"),
                hindiTokens = listOf("भा", "र", "त"),
                seamOnInnerSide = true
            )

            drawCoreBoatTailAndEngines(
                cx = cx,
                halfW = coreHW,
                topY = yCoreBottom,
                boatTailBottomY = yBoatTailBottom,
                nozzleBottomY = yCenterNozzleBottom
            )

            drawCoreStage(
                cx = cx,
                halfW = coreHW,
                topY = yCoreTop,
                bottomY = yCoreBottom
            )

            drawAttachmentBeams(
                cx = cx,
                coreHalfW = coreHW,
                leftBoosterCx = leftBoosterCx,
                rightBoosterCx = rightBoosterCx,
                boosterHalfW = boosterHW,
                topY = yCoreTop,
                bottomY = yCoreBottom
            )

            drawTrussSection(
                cx = cx,
                halfW = coreHW * 1.02f,
                topY = yTrussTop,
                bottomY = yTrussBottom
            )

            drawCryoStage(
                cx = cx,
                halfW = coreHW * 0.94f,
                topY = yCryoTop,
                bottomY = yCryoBottom
            )

            drawEquipmentRing(
                cx = cx,
                halfW = coreHW * 1.04f,
                topY = yEquipTop,
                bottomY = yEquipBottom
            )

            drawFairing(
                cx = cx,
                fairHalfW = fairHW,
                coreHalfW = coreHW,
                tipY = yFairTip,
                ogiveEndY = yFairOgiveEnd,
                cylEndY = yFairCylEnd,
                shoulderEndY = yFairShoulderEnd
            )
        }
    }
}

private fun DrawScope.drawFairing(
    cx: Float,
    fairHalfW: Float,
    coreHalfW: Float,
    tipY: Float,
    ogiveEndY: Float,
    cylEndY: Float,
    shoulderEndY: Float
) {
    val totalH = shoulderEndY - tipY
    val fairing = Path().apply {
        moveTo(cx, tipY)
        cubicTo(
            cx - fairHalfW * 0.05f, tipY + totalH * 0.07f,
            cx - fairHalfW * 0.68f, tipY + totalH * 0.34f,
            cx - fairHalfW, ogiveEndY
        )
        lineTo(cx - fairHalfW, cylEndY)
        quadraticBezierTo(
            cx - fairHalfW * 0.98f,
            cylEndY + (shoulderEndY - cylEndY) * 0.55f,
            cx - coreHalfW, shoulderEndY
        )
        lineTo(cx + coreHalfW, shoulderEndY)
        quadraticBezierTo(
            cx + fairHalfW * 0.98f,
            cylEndY + (shoulderEndY - cylEndY) * 0.55f,
            cx + fairHalfW, cylEndY
        )
        lineTo(cx + fairHalfW, ogiveEndY)
        cubicTo(
            cx + fairHalfW * 0.68f, tipY + totalH * 0.34f,
            cx + fairHalfW * 0.05f, tipY + totalH * 0.07f,
            cx, tipY
        )
        close()
    }

    drawPath(fairing, brush = bodyBrush(cx - fairHalfW, cx + fairHalfW))
    drawPath(fairing, color = LineDark.copy(alpha = 0.55f), style = Stroke(width = 1.05f))

    drawLine(
        color = LineSoft.copy(alpha = 0.55f),
        start = Offset(cx, tipY + totalH * 0.20f),
        end = Offset(cx, shoulderEndY),
        strokeWidth = 0.8f
    )

    listOf(0.30f, 0.55f, 0.79f).forEach { t ->
        val y = ogiveEndY + (cylEndY - ogiveEndY) * t
        drawLine(
            color = LineSoft.copy(alpha = 0.22f),
            start = Offset(cx - fairHalfW, y),
            end = Offset(cx + fairHalfW, y),
            strokeWidth = 0.65f
        )
    }

    // Flag centered nicely
    val flagW = fairHalfW * 1.05f
    val flagH = flagW * 0.58f
    val flagLeft = cx - flagW / 2f
    val flagTop = ogiveEndY + (cylEndY - ogiveEndY) * 0.15f // Moved down slightly to balance space
    val bandH = flagH / 3f

    drawRect(FlagSaffron, Offset(flagLeft, flagTop), Size(flagW, bandH))
    drawRect(Color.White, Offset(flagLeft, flagTop + bandH), Size(flagW, bandH))
    drawRect(FlagGreen, Offset(flagLeft, flagTop + bandH * 2f), Size(flagW, bandH))

    val chakraCenter = Offset(cx, flagTop + bandH * 1.5f)
    val chakraR = bandH * 0.38f
    drawCircle(FlagBlue, chakraR, chakraCenter, style = Stroke(width = 0.95f))
    repeat(24) { i ->
        val angle = Math.toRadians(i * 15.0 - 90.0)
        drawLine(
            color = FlagBlue,
            start = chakraCenter,
            end = Offset(
                chakraCenter.x + cos(angle).toFloat() * chakraR * 0.84f,
                chakraCenter.y + sin(angle).toFloat() * chakraR * 0.84f
            ),
            strokeWidth = 0.45f
        )
    }

    // Emblem and patch dot removed as requested.

    val rivetY = shoulderEndY - (shoulderEndY - cylEndY) * 0.15f
    repeat(13) { i ->
        val t = i / 12f
        val x = cx - fairHalfW * 0.82f + fairHalfW * 1.64f * t
        drawCircle(
            color = LineDark.copy(alpha = 0.9f),
            radius = fairHalfW * 0.014f,
            center = Offset(x, rivetY)
        )
    }
}

private fun DrawScope.drawEquipmentRing(
    cx: Float,
    halfW: Float,
    topY: Float,
    bottomY: Float
) {
    val h = bottomY - topY
    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(RingDark, RingLight, RingLight, RingDark),
            startX = cx - halfW,
            endX = cx + halfW
        ),
        topLeft = Offset(cx - halfW, topY),
        size = Size(halfW * 2f, h)
    )

    drawRect(
        color = LineDark.copy(alpha = 0.45f),
        topLeft = Offset(cx - halfW, topY),
        size = Size(halfW * 2f, h * 0.11f)
    )
    drawRect(
        color = LineDark.copy(alpha = 0.45f),
        topLeft = Offset(cx - halfW, bottomY - h * 0.11f),
        size = Size(halfW * 2f, h * 0.11f)
    )

    repeat(24) { i ->
        val x = cx - halfW + (i + 1) * (halfW * 2f / 25f)
        drawCircle(
            color = WhiteHi.copy(alpha = 0.85f),
            radius = h * 0.050f,
            center = Offset(x, topY + h * 0.16f)
        )
    }

    repeat(20) { i ->
        val x = cx - halfW + i * (halfW * 2f / 20f)
        drawLine(
            color = LineSoft.copy(alpha = 0.18f),
            start = Offset(x, topY + h * 0.38f),
            end = Offset(x, bottomY),
            strokeWidth = 0.45f
        )
    }

    drawRect(
        color = LineDark.copy(alpha = 0.55f),
        topLeft = Offset(cx - halfW, topY),
        size = Size(halfW * 2f, h),
        style = Stroke(width = 0.95f)
    )
}

private fun DrawScope.drawCryoStage(
    cx: Float,
    halfW: Float,
    topY: Float,
    bottomY: Float
) {
    val h = bottomY - topY
    drawRect(
        brush = bodyBrush(cx - halfW, cx + halfW),
        topLeft = Offset(cx - halfW, topY),
        size = Size(halfW * 2f, h)
    )

    repeat(5) { i ->
        val y = topY + (i + 1) * (h / 6f)
        drawLine(
            color = LineSoft.copy(alpha = 0.22f),
            start = Offset(cx - halfW, y),
            end = Offset(cx + halfW, y),
            strokeWidth = 0.6f
        )
    }

    drawLine(
        color = LineSoft.copy(alpha = 0.28f),
        start = Offset(cx, topY),
        end = Offset(cx, bottomY),
        strokeWidth = 0.6f
    )

    // Only Hindi ISRO centered perfectly
    val logoY = topY + h * 0.50f // Moved down since mission badge is gone
    drawIntoCanvas { canvas ->
        val nc = canvas.nativeCanvas
        val pOrange = NativePaint().apply {
            isAntiAlias = true
            color = IsroOrange.toArgb()
            textAlign = NativePaint.Align.CENTER
            textSize = halfW * 0.35f // Slightly larger since it's the only text here
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
        // Centered cx
        nc.drawText("इसरो", cx, logoY, pOrange)
    }

    // LVM3 Mission Badge removed as requested.

    drawRect(
        color = LineDark.copy(alpha = 0.55f),
        topLeft = Offset(cx - halfW, topY),
        size = Size(halfW * 2f, h),
        style = Stroke(width = 0.95f)
    )
}

private fun DrawScope.drawTrussSection(
    cx: Float,
    halfW: Float,
    topY: Float,
    bottomY: Float
) {
    val h = bottomY - topY
    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(TrussDark, TrussMid, TrussMid, TrussDark),
            startX = cx - halfW,
            endX = cx + halfW
        ),
        topLeft = Offset(cx - halfW, topY),
        size = Size(halfW * 2f, h)
    )

    val cells = 12
    val cw = halfW * 2f / cells
    repeat(cells) { i ->
        val x0 = cx - halfW + i * cw
        val xm = x0 + cw / 2f
        val x1 = x0 + cw
        val y0 = topY + h * 0.10f
        val y1 = bottomY - h * 0.10f
        drawLine(TrussLine, Offset(x0, y0), Offset(xm, y1), 0.95f)
        drawLine(TrussLine, Offset(x1, y0), Offset(xm, y1), 0.95f)
    }

    drawRect(
        color = LineDark.copy(alpha = 0.65f),
        topLeft = Offset(cx - halfW, topY),
        size = Size(halfW * 2f, h * 0.12f)
    )
    drawRect(
        color = LineDark.copy(alpha = 0.65f),
        topLeft = Offset(cx - halfW, bottomY - h * 0.12f),
        size = Size(halfW * 2f, h * 0.12f)
    )

    drawRect(
        color = LineDark.copy(alpha = 0.70f),
        topLeft = Offset(cx - halfW, topY),
        size = Size(halfW * 2f, h),
        style = Stroke(width = 1f)
    )
}

private fun DrawScope.drawCoreStage(
    cx: Float,
    halfW: Float,
    topY: Float,
    bottomY: Float
) {
    val h = bottomY - topY

    drawRect(
        brush = bodyBrush(cx - halfW, cx + halfW),
        topLeft = Offset(cx - halfW, topY),
        size = Size(halfW * 2f, h)
    )

    val upperRibEnd = topY + h * 0.30f
    repeat(30) { i ->
        val x = cx - halfW + i * ((halfW * 2f) / 30f)
        drawLine(
            color = LineSoft.copy(alpha = 0.30f),
            start = Offset(x, topY),
            end = Offset(x, upperRibEnd),
            strokeWidth = 0.55f
        )
    }

    repeat(4) { i ->
        val y = topY + h * (0.08f + i * 0.04f)
        drawLine(
            color = LineSoft.copy(alpha = 0.18f),
            start = Offset(cx - halfW, y),
            end = Offset(cx + halfW, y),
            strokeWidth = 0.55f
        )
    }

    val racewayW = halfW * 0.10f
    val racewayTop = topY + h * 0.17f
    val racewayBottom = topY + h * 0.86f
    drawRoundRect(
        brush = Brush.horizontalGradient(
            colors = listOf(WhiteShadow, WhiteHi, WhiteShadow),
            startX = cx - racewayW / 2f,
            endX = cx + racewayW / 2f
        ),
        topLeft = Offset(cx - racewayW / 2f, racewayTop),
        size = Size(racewayW, racewayBottom - racewayTop),
        cornerRadius = CornerRadius(racewayW * 0.25f, racewayW * 0.25f)
    )
    drawRoundRect(
        color = LineSoft.copy(alpha = 0.7f),
        topLeft = Offset(cx - racewayW / 2f, racewayTop),
        size = Size(racewayW, racewayBottom - racewayTop),
        cornerRadius = CornerRadius(racewayW * 0.25f, racewayW * 0.25f),
        style = Stroke(width = 0.7f)
    )

    repeat(5) { i ->
        val y = racewayTop + (i + 1) * ((racewayBottom - racewayTop) / 6f)
        drawLine(
            color = LineSoft.copy(alpha = 0.25f),
            start = Offset(cx - racewayW * 0.85f, y),
            end = Offset(cx + racewayW * 0.85f, y),
            strokeWidth = 0.45f
        )
    }

    // Moved the Vindusha text UP to topY + h * 0.11f
    // This places it perfectly in the open white space below the black truss ring
    val titleY = topY + h * 0.11f
    drawIntoCanvas { canvas ->
        val nc = canvas.nativeCanvas

        val p1 = NativePaint().apply {
            isAntiAlias = true
            color = LineDark.toArgb()
            textAlign = NativePaint.Align.CENTER
            textSize = halfW * 0.40f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }

        nc.drawText("Vindusha", cx, titleY, p1)
    }

    // Adjusted the starting Y coordinate of the vertical lines lower
    // so they do not touch the text at all
    val upperPanelY = topY + h * 0.53f
    repeat(24) { i ->
        val x = cx - halfW + i * ((halfW * 2f) / 24f)
        drawLine(
            color = LineSoft.copy(alpha = 0.28f),
            start = Offset(x, upperPanelY),
            end = Offset(x, topY + h * 0.74f),
            strokeWidth = 0.55f
        )
    }

    val midBandTop = topY + h * 0.75f
    val midBandBottom = topY + h * 0.80f
    drawRect(
        color = WhiteMid,
        topLeft = Offset(cx - halfW, midBandTop),
        size = Size(halfW * 2f, midBandBottom - midBandTop)
    )
    drawLine(
        LineMid.copy(alpha = 0.55f),
        Offset(cx - halfW, midBandTop),
        Offset(cx + halfW, midBandTop),
        1f
    )
    drawLine(
        LineMid.copy(alpha = 0.55f),
        Offset(cx - halfW, midBandBottom),
        Offset(cx + halfW, midBandBottom),
        1f
    )

    drawServicePanel(
        left = cx - halfW * 0.53f,
        top = topY + h * 0.57f,
        width = halfW * 0.22f,
        height = h * 0.08f
    )
    drawServicePanel(
        left = cx + halfW * 0.30f,
        top = topY + h * 0.57f,
        width = halfW * 0.22f,
        height = h * 0.08f
    )

    drawServicePanel(
        left = cx - halfW * 0.53f,
        top = topY + h * 0.29f,
        width = halfW * 0.24f,
        height = h * 0.11f
    )
    drawServicePanel(
        left = cx + halfW * 0.28f,
        top = topY + h * 0.29f,
        width = halfW * 0.24f,
        height = h * 0.11f
    )

    drawRect(
        color = LineDark.copy(alpha = 0.60f),
        topLeft = Offset(cx - halfW, topY),
        size = Size(halfW * 2f, h),
        style = Stroke(width = 1f)
    )
}

private fun DrawScope.drawServicePanel(
    left: Float,
    top: Float,
    width: Float,
    height: Float
) {
    drawRect(
        color = WhiteWarm,
        topLeft = Offset(left, top),
        size = Size(width, height)
    )
    drawRect(
        color = LineSoft.copy(alpha = 0.75f),
        topLeft = Offset(left, top),
        size = Size(width, height),
        style = Stroke(width = 0.7f)
    )
    drawLine(
        color = LineSoft.copy(alpha = 0.25f),
        start = Offset(left + width / 2f, top),
        end = Offset(left + width / 2f, top + height),
        strokeWidth = 0.45f
    )
    drawLine(
        color = LineSoft.copy(alpha = 0.25f),
        start = Offset(left, top + height / 2f),
        end = Offset(left + width, top + height / 2f),
        strokeWidth = 0.45f
    )
}

private fun DrawScope.drawAttachmentBeams(
    cx: Float,
    coreHalfW: Float,
    leftBoosterCx: Float,
    rightBoosterCx: Float,
    boosterHalfW: Float,
    topY: Float,
    bottomY: Float
) {
    val y1 = topY + (bottomY - topY) * 0.34f
    val y2 = topY + (bottomY - topY) * 0.70f
    val beamH = coreHalfW * 0.060f

    listOf(y1, y2).forEach { y ->
        val leftStart = leftBoosterCx + boosterHalfW
        val leftEnd = cx - coreHalfW
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(WhiteHi, WhiteMid, WhiteShadow),
                startY = y,
                endY = y + beamH
            ),
            topLeft = Offset(leftStart, y),
            size = Size(leftEnd - leftStart, beamH)
        )
        drawRect(
            color = LineSoft.copy(alpha = 0.65f),
            topLeft = Offset(leftStart, y),
            size = Size(leftEnd - leftStart, beamH),
            style = Stroke(width = 0.7f)
        )

        val rightStart = cx + coreHalfW
        val rightEnd = rightBoosterCx - boosterHalfW
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(WhiteHi, WhiteMid, WhiteShadow),
                startY = y,
                endY = y + beamH
            ),
            topLeft = Offset(rightStart, y),
            size = Size(rightEnd - rightStart, beamH)
        )
        drawRect(
            color = LineSoft.copy(alpha = 0.65f),
            topLeft = Offset(rightStart, y),
            size = Size(rightEnd - rightStart, beamH),
            style = Stroke(width = 0.7f)
        )
    }
}

private fun DrawScope.drawBooster(
    centerX: Float,
    halfW: Float,
    tipY: Float,
    noseEndY: Float,
    skirtTopY: Float,
    baseY: Float,
    nozzleBottomY: Float,
    englishTokens: List<String>,
    hindiTokens: List<String>,
    seamOnInnerSide: Boolean
) {
    val barrelH = baseY - noseEndY
    val noseH = noseEndY - tipY

    val nose = Path().apply {
        moveTo(centerX, tipY)
        cubicTo(
            centerX - halfW * 0.08f, tipY + noseH * 0.10f,
            centerX - halfW * 0.86f, tipY + noseH * 0.66f,
            centerX - halfW, noseEndY
        )
        lineTo(centerX + halfW, noseEndY)
        cubicTo(
            centerX + halfW * 0.86f, tipY + noseH * 0.66f,
            centerX + halfW * 0.08f, tipY + noseH * 0.10f,
            centerX, tipY
        )
        close()
    }

    drawPath(nose, brush = bodyBrush(centerX - halfW, centerX + halfW))
    drawPath(nose, color = LineDark.copy(alpha = 0.55f), style = Stroke(width = 0.95f))

    drawRect(
        brush = bodyBrush(centerX - halfW, centerX + halfW),
        topLeft = Offset(centerX - halfW, noseEndY),
        size = Size(halfW * 2f, barrelH)
    )

    repeat(7) { i ->
        val x = centerX - halfW + (i + 1) * (halfW * 2f / 8f)
        drawLine(
            color = LineSoft.copy(alpha = 0.14f),
            start = Offset(x, noseEndY),
            end = Offset(x, baseY),
            strokeWidth = 0.45f
        )
    }

    val seamX = if (seamOnInnerSide && centerX < size.width / 2f) {
        centerX + halfW * 0.36f
    } else if (seamOnInnerSide) {
        centerX - halfW * 0.36f
    } else {
        centerX
    }

    drawLine(
        color = LineSoft.copy(alpha = 0.5f),
        start = Offset(seamX, noseEndY),
        end = Offset(seamX, baseY - (baseY - noseEndY) * 0.03f),
        strokeWidth = 0.8f
    )

    val r1 = noseEndY + barrelH * 0.18f
    val r2 = noseEndY + barrelH * 0.64f
    val r3 = noseEndY + barrelH * 0.88f

    listOf(r1, r2, r3).forEach { y ->
        drawLine(
            color = LineDark.copy(alpha = 0.55f),
            start = Offset(centerX - halfW, y),
            end = Offset(centerX + halfW, y),
            strokeWidth = 0.95f
        )
        repeat(8) { i ->
            val x = centerX - halfW + (i + 1) * (halfW * 2f / 9f)
            drawCircle(
                color = LineSoft.copy(alpha = 0.55f),
                radius = halfW * 0.015f,
                center = Offset(x, y)
            )
        }
    }

    drawRect(
        color = LineDark.copy(alpha = 0.58f),
        topLeft = Offset(centerX - halfW, noseEndY),
        size = Size(halfW * 2f, barrelH),
        style = Stroke(width = 0.95f)
    )

    val flare = halfW * 0.08f
    val skirt = Path().apply {
        moveTo(centerX - halfW, skirtTopY)
        lineTo(centerX - halfW - flare, baseY)
        lineTo(centerX + halfW + flare, baseY)
        lineTo(centerX + halfW, skirtTopY)
        close()
    }

    drawPath(
        path = skirt,
        brush = bodyBrush(centerX - halfW - flare, centerX + halfW + flare)
    )
    drawPath(
        path = skirt,
        color = LineDark.copy(alpha = 0.60f),
        style = Stroke(width = 0.8f)
    )

    repeat(12) { i ->
        val t = i / 11f
        val xt = centerX - halfW + (halfW * 2f) * t
        val xb = centerX - (halfW + flare) + (halfW + flare) * 2f * t
        drawLine(
            color = LineSoft.copy(alpha = 0.34f),
            start = Offset(xt, skirtTopY + (baseY - skirtTopY) * 0.08f),
            end = Offset(xb, baseY - (baseY - skirtTopY) * 0.10f),
            strokeWidth = 0.55f
        )
    }

    draw3DBellNozzle(
        cx = centerX,
        topY = baseY,
        bottomY = nozzleBottomY,
        topHalf = halfW * 0.20f,
        bottomHalf = halfW * 0.38f
    )

    // Adjusted vertical text placement for boosters to ensure even alignment
    val topTextSectionHeight = barrelH * 0.40f
    val topTextGap = topTextSectionHeight / (englishTokens.size)

    drawVerticalTokenText(
        centerX = centerX,
        startY = noseEndY + barrelH * 0.10f, // Start a bit higher
        tokens = englishTokens,
        textSize = halfW * 0.26f,
        gap = topTextGap - (halfW * 0.26f), // Distributed gap
        color = LineDark,
        bold = false
    )

    val bottomTextSectionHeight = barrelH * 0.20f
    val bottomTextGap = bottomTextSectionHeight / hindiTokens.size

    drawVerticalTokenText(
        centerX = centerX,
        startY = noseEndY + barrelH * 0.65f, // Move hindi up slightly to balance
        tokens = hindiTokens,
        textSize = halfW * 0.20f,
        gap = bottomTextGap - (halfW * 0.20f), // Distributed gap
        color = LineDark,
        bold = false
    )
}

private fun DrawScope.drawCoreBoatTailAndEngines(
    cx: Float,
    halfW: Float,
    topY: Float,
    boatTailBottomY: Float,
    nozzleBottomY: Float
) {
    val boatBottomHalf = halfW * 0.46f
    val boatTail = Path().apply {
        moveTo(cx - halfW, topY)
        lineTo(cx - boatBottomHalf, boatTailBottomY)
        lineTo(cx + boatBottomHalf, boatTailBottomY)
        lineTo(cx + halfW, topY)
        close()
    }

    drawPath(
        path = boatTail,
        brush = bodyBrush(cx - halfW, cx + halfW)
    )
    drawPath(
        path = boatTail,
        color = LineDark.copy(alpha = 0.60f),
        style = Stroke(width = 0.85f)
    )

    val plateY = boatTailBottomY - (boatTailBottomY - topY) * 0.04f
    drawLine(
        color = LineDark.copy(alpha = 0.7f),
        start = Offset(cx - boatBottomHalf, plateY),
        end = Offset(cx + boatBottomHalf, plateY),
        strokeWidth = 1f
    )

    val nozzleSep = halfW * 0.31f
    listOf(-nozzleSep, nozzleSep).forEach { ox ->
        draw3DBellNozzle(
            cx = cx + ox,
            topY = boatTailBottomY,
            bottomY = nozzleBottomY,
            topHalf = halfW * 0.09f,
            bottomHalf = halfW * 0.18f
        )
    }
}

private fun DrawScope.draw3DBellNozzle(
    cx: Float,
    topY: Float,
    bottomY: Float,
    topHalf: Float,
    bottomHalf: Float
) {
    val h = bottomY - topY

    val nozzle = Path().apply {
        moveTo(cx - topHalf, topY)
        cubicTo(
            cx - topHalf * 1.0f, topY + h * 0.4f,
            cx - bottomHalf * 0.8f, bottomY - h * 0.2f,
            cx - bottomHalf, bottomY
        )
        lineTo(cx + bottomHalf, bottomY)
        cubicTo(
            cx + bottomHalf * 0.8f, bottomY - h * 0.2f,
            cx + topHalf * 1.0f, topY + h * 0.4f,
            cx + topHalf, topY
        )
        close()
    }

    drawPath(
        path = nozzle,
        brush = Brush.horizontalGradient(
            colors = listOf(
                NozzleMetalDark,
                NozzleMetalMid,
                NozzleMetalHi,
                NozzleMetalMid,
                NozzleMetalDark
            ),
            startX = cx - bottomHalf,
            endX = cx + bottomHalf
        )
    )
    drawPath(nozzle, color = Color(0xFF140B05), style = Stroke(width = 0.8f))

    val numRings = 4
    for (i in 1..numRings) {
        val t = i / (numRings + 1f)
        val ringY = topY + h * t
        val easeT = t * t * (3 - 2 * t)
        val ringHalfW = topHalf + (bottomHalf - topHalf) * easeT * 0.85f
        val ovalH = ringHalfW * 0.2f

        drawArc(
            color = Color(0xFF2A1A10).copy(alpha = 0.5f),
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(cx - ringHalfW, ringY - ovalH / 2f),
            size = Size(ringHalfW * 2f, ovalH),
            style = Stroke(width = 0.6f)
        )
    }

    val lipH = bottomHalf * 0.28f
    val lipRectTop = bottomY - lipH / 2f

    drawOval(
        color = Color(0xFF0A0502),
        topLeft = Offset(cx - bottomHalf, lipRectTop),
        size = Size(bottomHalf * 2f, lipH)
    )

    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(Color.Black, Color.Transparent),
            center = Offset(cx, bottomY - lipH * 0.1f),
            radius = bottomHalf * 0.7f
        ),
        topLeft = Offset(cx - bottomHalf, lipRectTop),
        size = Size(bottomHalf * 2f, lipH)
    )

    drawArc(
        color = NozzleMetalHi,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(cx - bottomHalf, lipRectTop),
        size = Size(bottomHalf * 2f, lipH),
        style = Stroke(width = 1.2f)
    )
}

private fun DrawScope.drawAmbientShadows(
    cx: Float,
    halfW: Float,
    topY: Float,
    bottomY: Float
) {
    val shadowW = halfW * 0.24f
    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Black.copy(alpha = 0.22f), Color.Transparent),
            startX = cx - halfW - shadowW,
            endX = cx - halfW
        ),
        topLeft = Offset(cx - halfW - shadowW, topY),
        size = Size(shadowW, bottomY - topY)
    )

    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.22f)),
            startX = cx + halfW,
            endX = cx + halfW + shadowW
        ),
        topLeft = Offset(cx + halfW, topY),
        size = Size(shadowW, bottomY - topY)
    )
}

private fun DrawScope.bodyBrush(left: Float, right: Float): Brush {
    return Brush.horizontalGradient(
        colors = listOf(
            WhiteEdge,
            WhiteMid,
            WhiteHi,
            WhiteWarm,
            WhiteHi,
            WhiteMid,
            WhiteEdge
        ),
        startX = left,
        endX = right
    )
}

private fun DrawScope.drawVerticalTokenText(
    centerX: Float,
    startY: Float,
    tokens: List<String>,
    textSize: Float,
    gap: Float,
    color: Color,
    bold: Boolean
) {
    drawIntoCanvas { canvas ->
        val p = NativePaint().apply {
            isAntiAlias = true
            this.color = color.toArgb()
            textAlign = NativePaint.Align.CENTER
            this.textSize = textSize
            typeface = Typeface.create(
                Typeface.SANS_SERIF,
                if (bold) Typeface.BOLD else Typeface.NORMAL
            )
        }
        tokens.forEachIndexed { i, token ->
            val y = startY + i * (textSize + gap)
            canvas.nativeCanvas.drawText(token, centerX, y, p)
        }
    }
}