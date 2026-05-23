package com.vimla.myapplication.Screen.SpaceMaintenance

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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

// ─── Colour Palette ────────────────────────────────────────────────────────────
private val SpaceBg       = Color(0xFF03070B)

// Armour / top-deck (lighter, hit by light from above)
private val ArmorEdge     = Color(0xFFD6DAE0)
private val ArmorMid      = Color(0xFFB4BAC3)
private val ArmorWarm     = Color(0xFF909AA6)

// Main hull sides (darker)
private val HullHi        = Color(0xFF4A525D)
private val HullMid       = Color(0xFF333A44)
private val HullDark      = Color(0xFF1C222B)
private val HullBlack     = Color(0xFF0F141A)

// Cockpit glass
private val GlassHi       = Color(0xFFC3D8EA)
private val GlassMid      = Color(0xFF7893AF)
private val GlassDark     = Color(0xFF26384B)

// Engine / weapon accents
private val AccentRed     = Color(0xFFE33B2E)
private val AccentRedDeep = Color(0xFF6E120E)

// ─── Composable Entry Point ────────────────────────────────────────────────────
@Composable
fun Rocket(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBg)
            .padding(innerPadding)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(SpaceBg)
            drawThreeQuarterFrontLeftRocket()
        }
    }
}

// ─── Master Draw Orchestrator ──────────────────────────────────────────────────
//
//  Reference-image analysis:
//    • Nose/front is at LOWER-LEFT — we see the front FACE head-on (key!)
//    • The ship body climbs diagonally to UPPER-RIGHT toward the engine
//    • Top deck is a narrow visible parallelogram (viewer is slightly above)
//    • Cockpit glass spans the front-face AND bleeds into the upper side hull
//    • Rear wing sweeps down-and-back from the mid-body lower edge
//    • Vertical fin rises above the hull at the rear
//    • Engine nacelle (cylinder) is at far right, flame shoots right
//    • Twin gun barrels project forward-left from the nose face
//
//  Coordinate origin: x,y = top-left of the drawing bounding box
//  All child functions receive (x, y, w, h) and use fractional offsets.
//
private fun DrawScope.drawThreeQuarterFrontLeftRocket() {
    // ── Place the ship so the nose is lower-left, engine is upper-right ──────
    val x = size.width  * 0.02f
    val y = size.height * 0.12f
    val w = size.width  * 0.90f
    val h = size.height * 0.74f

    // Back-to-front paint order
    drawEngineGlow(x, y, w, h)          // 1. soft red ambient glow around engine
    drawCastShadow(x, y, w, h)          // 2. drop shadow below ship
    drawRearSweptWing(x, y, w, h)       // 3. large wing behind main hull
    drawBottomKeel(x, y, w, h)          // 4. underbelly / chin extension
    drawMainSideHull(x, y, w, h)        // 5. PRIMARY large side face of hull
    drawTopDeck(x, y, w, h)             // 6. visible top surface (3-D effect)
    drawFrontFace(x, y, w, h)           // 7. ★ THE VISIBLE NOSE FACE — defines the 3/4 angle
    drawCockpitWindow(x, y, w, h)       // 8. large angular cockpit glass
    drawTopFin(x, y, w, h)              // 9. vertical stabiliser at rear
    drawEngineNacelle(x, y, w, h)       // 10. cylindrical engine body
    drawFrontGrille(x, y, w, h)         // 11. red weapon/vent block on chin
    drawFrontGunBarrels(x, y, w, h)     // 12. protruding gun barrels toward viewer
    drawPanelDetails(x, y, w, h)        // 13. surface panel lines, rivets, boxes
    drawEngineFlame(x, y, w, h)         // 14. engine exhaust flame (on top of everything)
}

// ─── 1. Engine Ambient Glow ────────────────────────────────────────────────────
private fun DrawScope.drawEngineGlow(x: Float, y: Float, w: Float, h: Float) {
    val cx = Offset(x + w * 0.980f, y + h * 0.390f)
    val r  = w * 0.145f
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                AccentRed.copy(alpha = 0.70f),
                AccentRed.copy(alpha = 0.26f),
                Color.Transparent
            ),
            center = cx, radius = r
        ),
        radius = r, center = cx,
        blendMode = BlendMode.Screen
    )
}

// ─── 2. Cast Shadow ────────────────────────────────────────────────────────────
private fun DrawScope.drawCastShadow(x: Float, y: Float, w: Float, h: Float) {
    val shadow = Path().apply {
        // shadow follows the diagonal hull outline, offset downward
        moveTo(x + w * 0.10f, y + h * 0.85f)
        lineTo(x + w * 0.42f, y + h * 0.96f)
        lineTo(x + w * 0.88f, y + h * 0.82f)
        lineTo(x + w * 0.84f, y + h * 0.92f)
        lineTo(x + w * 0.40f, y + h * 1.06f)
        lineTo(x + w * 0.12f, y + h * 0.96f)
        close()
    }
    drawPath(shadow, color = Color.Black.copy(alpha = 0.12f))
}

// ─── 3. Rear Swept Wing ────────────────────────────────────────────────────────
//  Drawn BEFORE the main hull so the hull paints on top of its root.
private fun DrawScope.drawRearSweptWing(x: Float, y: Float, w: Float, h: Float) {
    // Large delta wing, sweeps from mid-body downward-and-aft
    val wing = Path().apply {
        moveTo(x + w * 0.46f, y + h * 0.68f)   // wing root front (at hull lower edge)
        lineTo(x + w * 0.65f, y + h * 0.70f)   // wing root rear
        lineTo(x + w * 0.94f, y + h * 0.88f)   // wingtip (far aft)
        lineTo(x + w * 0.90f, y + h * 0.98f)   // wingtip trailing edge
        lineTo(x + w * 0.60f, y + h * 0.85f)   // inner trailing edge
        lineTo(x + w * 0.42f, y + h * 0.80f)   // root trailing edge
        close()
    }
    drawPath(
        wing,
        brush = Brush.linearGradient(
            colors = listOf(HullHi, HullMid, HullDark, HullBlack),
            start  = Offset(x + w * 0.46f, y + h * 0.68f),
            end    = Offset(x + w * 0.93f, y + h * 0.97f)
        )
    )
    // Edge highlight
    drawLine(
        color = Color.White.copy(alpha = 0.10f),
        start = Offset(x + w * 0.48f, y + h * 0.70f),
        end   = Offset(x + w * 0.88f, y + h * 0.90f),
        strokeWidth = size.minDimension * 0.0014f
    )
    // Secondary crease panel line
    drawLine(
        color = Color.White.copy(alpha = 0.06f),
        start = Offset(x + w * 0.52f, y + h * 0.74f),
        end   = Offset(x + w * 0.80f, y + h * 0.80f),
        strokeWidth = size.minDimension * 0.0010f
    )
}

// ─── 4. Bottom Keel / Chin Block ──────────────────────────────────────────────
private fun DrawScope.drawBottomKeel(x: Float, y: Float, w: Float, h: Float) {
    val keel = Path().apply {
        moveTo(x + w * 0.15f, y + h * 0.76f)   // front lower-left
        lineTo(x + w * 0.26f, y + h * 0.92f)   // chin front
        lineTo(x + w * 0.58f, y + h * 0.84f)   // chin rear
        lineTo(x + w * 0.66f, y + h * 0.70f)   // rear lower
        lineTo(x + w * 0.46f, y + h * 0.68f)   // mid lower
        lineTo(x + w * 0.22f, y + h * 0.72f)   // front lower
        close()
    }
    drawPath(
        keel,
        brush = Brush.linearGradient(
            colors = listOf(HullMid, HullDark, HullBlack),
            start  = Offset(x + w * 0.15f, y + h * 0.70f),
            end    = Offset(x + w * 0.60f, y + h * 0.92f)
        )
    )
}

// ─── 5. Main Side Hull ────────────────────────────────────────────────────────
//
//  This is the largest face — the main body of the ship as seen from the side.
//  Its LEFT edge is the RIGHT edge of the front face (they share the seam).
//  Its RIGHT edge connects to the engine nacelle.
//  The hull is tilted: front (left) is lower, rear (right) is higher.
//
private fun DrawScope.drawMainSideHull(x: Float, y: Float, w: Float, h: Float) {
    val hull = Path().apply {
        moveTo(x + w * 0.26f, y + h * 0.22f)   // front-top  (shared with front face)
        lineTo(x + w * 0.82f, y + h * 0.13f)   // rear-top
        lineTo(x + w * 0.90f, y + h * 0.23f)   // rear-top-right corner
        lineTo(x + w * 0.90f, y + h * 0.60f)   // rear-bottom-right corner
        lineTo(x + w * 0.82f, y + h * 0.68f)   // rear-bottom
        lineTo(x + w * 0.26f, y + h * 0.74f)   // front-bottom (shared with front face)
        close()
    }
    drawPath(
        hull,
        brush = Brush.linearGradient(
            colors = listOf(HullHi, HullMid, HullDark, HullBlack),
            start  = Offset(x + w * 0.26f, y + h * 0.20f),
            end    = Offset(x + w * 0.88f, y + h * 0.72f)
        )
    )

    // ── Perspective converging panel seams (subtle, go from front to rear) ──
    listOf(Pair(0.35f, 0.35f), Pair(0.62f, 0.60f)).forEach { (tfY, tbY) ->
        val fX = x + w * 0.26f;  val fY = y + h * (0.22f + (0.74f - 0.22f) * tfY)
        val rX = x + w * 0.90f;  val rY = y + h * (0.23f + (0.60f - 0.23f) * tbY)
        drawLine(
            color = Color.White.copy(alpha = 0.07f),
            start = Offset(fX, fY), end = Offset(rX, rY),
            strokeWidth = size.minDimension * 0.0010f
        )
    }

    // Tech panel box mid-hull
    drawRoundRect(
        color = Color(0xFF1E272F),
        topLeft      = Offset(x + w * 0.52f, y + h * 0.29f),
        size         = Size(w * 0.086f, h * 0.110f),
        cornerRadius = CornerRadius(size.minDimension * 0.0020f, size.minDimension * 0.0020f)
    )
    // Small vent slits inside tech panel
    repeat(3) { i ->
        drawLine(
            color = Color(0xFF0A1018),
            start = Offset(x + w * 0.534f, y + h * (0.308f + i * 0.026f)),
            end   = Offset(x + w * 0.590f, y + h * (0.308f + i * 0.026f)),
            strokeWidth = size.minDimension * 0.0016f
        )
    }
}

// ─── 6. Top Deck ──────────────────────────────────────────────────────────────
//
//  The thin visible top surface proves the ship is a 3-D object.
//  It's a narrow parallelogram: front edge = top edge of the front face,
//  rear edge = top edge of the hull at the back.
//
private fun DrawScope.drawTopDeck(x: Float, y: Float, w: Float, h: Float) {
    val deck = Path().apply {
        moveTo(x + w * 0.08f, y + h * 0.24f)   // front-outer-top (leftmost top of ship)
        lineTo(x + w * 0.26f, y + h * 0.22f)   // front-inner-top (right edge of front face top)
        lineTo(x + w * 0.82f, y + h * 0.13f)   // rear-inner-top (same as hull rearTop)
        lineTo(x + w * 0.74f, y + h * 0.09f)   // rear-outer-top (shows deck thickness)
        lineTo(x + w * 0.30f, y + h * 0.08f)   // mid-outer-top
        close()
    }
    drawPath(
        deck,
        brush = Brush.linearGradient(
            colors = listOf(ArmorEdge, ArmorMid, ArmorWarm, HullHi),
            start  = Offset(x + w * 0.08f, y + h * 0.08f),
            end    = Offset(x + w * 0.82f, y + h * 0.24f)
        )
    )
    // Top-deck highlight crease
    drawLine(
        color = Color.White.copy(alpha = 0.18f),
        start = Offset(x + w * 0.12f, y + h * 0.14f),
        end   = Offset(x + w * 0.76f, y + h * 0.10f),
        strokeWidth = size.minDimension * 0.0013f
    )
    // Dorsal equipment vent
    drawRoundRect(
        color        = Color(0xFF28333D),
        topLeft      = Offset(x + w * 0.34f, y + h * 0.083f),
        size         = Size(w * 0.082f, h * 0.038f),
        cornerRadius = CornerRadius(size.minDimension * 0.0017f, size.minDimension * 0.0017f)
    )
    // Dorsal port circle (matches reference image)
    drawCircle(
        color  = Color(0xFFCBD1D8),
        radius = size.minDimension * 0.021f,
        center = Offset(x + w * 0.630f, y + h * 0.110f)
    )
    drawCircle(
        color  = HullDark,
        radius = size.minDimension * 0.014f,
        center = Offset(x + w * 0.630f, y + h * 0.110f)
    )
}

// ─── 7. ★ Front Face — The 3/4 Angle Defining Shape ─────────────────────────
//
//  This is the face of the ship pointing toward the viewer.
//  In a pure SIDE PROFILE this face would be invisible (zero-width).
//  Making it a large visible polygon is THE fix for the front-left perspective.
//
//  Shape:  A hexagonal/trapezoidal panel on the LEFT of the canvas.
//          Its right edge is shared with the left edge of the main hull.
//          Its left edge is the nose outer edge facing the viewer.
//
//  Key corners:
//    FL_T = front-left-top   → leftmost upper corner of the nose face
//    FR_T = front-right-top  → upper seam with the main hull (and top deck)
//    FR_B = front-right-bot  → lower seam with the main hull
//    FL_B = front-left-bot   → leftmost lower corner of the nose face
//
private fun DrawScope.drawFrontFace(x: Float, y: Float, w: Float, h: Float) {
    // Outer (far-left) visible front face
    val frontFace = Path().apply {
        moveTo(x + w * 0.08f, y + h * 0.24f)   // FL_T  (joins top deck outer)
        lineTo(x + w * 0.26f, y + h * 0.22f)   // FR_T  (joins top deck / hull top)
        lineTo(x + w * 0.26f, y + h * 0.74f)   // FR_B  (joins hull bottom)
        lineTo(x + w * 0.08f, y + h * 0.78f)   // FL_B  (lower outer)
        lineTo(x + w * 0.03f, y + h * 0.60f)   // nose-lower-tip (pointed lower)
        lineTo(x + w * 0.03f, y + h * 0.40f)   // nose-upper-tip (pointed upper)
        close()
    }
    drawPath(
        frontFace,
        brush = Brush.linearGradient(
            colors = listOf(ArmorEdge, ArmorMid, HullHi, HullMid),
            start  = Offset(x + w * 0.04f, y + h * 0.22f),
            end    = Offset(x + w * 0.25f, y + h * 0.78f)
        )
    )

    // ── Chin recess / shadow on lower front face ─────────────────────────────
    val chinRecess = Path().apply {
        moveTo(x + w * 0.06f, y + h * 0.58f)
        lineTo(x + w * 0.18f, y + h * 0.52f)
        lineTo(x + w * 0.24f, y + h * 0.72f)
        lineTo(x + w * 0.12f, y + h * 0.76f)
        lineTo(x + w * 0.04f, y + h * 0.62f)
        close()
    }
    drawPath(chinRecess, color = HullBlack.copy(alpha = 0.65f))

    // ── Light edge highlight on upper front face ──────────────────────────────
    // (Light hits the top-left bevel of the nose face)
    drawLine(
        color = ArmorEdge.copy(alpha = 0.55f),
        start = Offset(x + w * 0.04f, y + h * 0.40f),
        end   = Offset(x + w * 0.24f, y + h * 0.22f),
        strokeWidth = size.minDimension * 0.0028f,
        cap   = StrokeCap.Round
    )
    // Secondary bevel catch-light
    drawLine(
        color = Color.White.copy(alpha = 0.14f),
        start = Offset(x + w * 0.04f, y + h * 0.40f),
        end   = Offset(x + w * 0.04f, y + h * 0.60f),
        strokeWidth = size.minDimension * 0.0016f,
        cap   = StrokeCap.Round
    )
}

// ─── 8. Cockpit Window ────────────────────────────────────────────────────────
//
//  The cockpit spans BOTH the front face AND the side hull — just as in the
//  reference image where the large angular glass bleeds across the corner.
//
private fun DrawScope.drawCockpitWindow(x: Float, y: Float, w: Float, h: Float) {
    // ── Dark outer frame ─────────────────────────────────────────────────────
    val frame = Path().apply {
        moveTo(x + w * 0.09f, y + h * 0.260f)   // top on front face
        lineTo(x + w * 0.26f, y + h * 0.228f)   // seam point (front face → side hull)
        lineTo(x + w * 0.42f, y + h * 0.248f)   // extends right on side hull
        lineTo(x + w * 0.40f, y + h * 0.500f)   // lower-right on side hull
        lineTo(x + w * 0.24f, y + h * 0.535f)   // back across seam
        lineTo(x + w * 0.09f, y + h * 0.505f)   // bottom on front face
        close()
    }
    drawPath(
        frame,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF1C2530), Color(0xFF0A1018), Color(0xFF1E2D3A)),
            start  = Offset(x + w * 0.10f, y + h * 0.23f),
            end    = Offset(x + w * 0.40f, y + h * 0.53f)
        )
    )

    // ── Glass panel ──────────────────────────────────────────────────────────
    val glass = Path().apply {
        moveTo(x + w * 0.104f, y + h * 0.276f)
        lineTo(x + w * 0.263f, y + h * 0.246f)
        lineTo(x + w * 0.406f, y + h * 0.262f)
        lineTo(x + w * 0.386f, y + h * 0.483f)
        lineTo(x + w * 0.242f, y + h * 0.516f)
        lineTo(x + w * 0.104f, y + h * 0.488f)
        close()
    }
    drawPath(
        glass,
        brush = Brush.linearGradient(
            colors = listOf(GlassHi, GlassMid, GlassDark, GlassDark),
            start  = Offset(x + w * 0.11f, y + h * 0.24f),
            end    = Offset(x + w * 0.39f, y + h * 0.51f)
        )
    )

    // ── Interior red console glow ─────────────────────────────────────────────
    val glow = Path().apply {
        moveTo(x + w * 0.25f, y + h * 0.28f)
        lineTo(x + w * 0.39f, y + h * 0.28f)
        lineTo(x + w * 0.37f, y + h * 0.46f)
        lineTo(x + w * 0.24f, y + h * 0.48f)
        close()
    }
    drawPath(
        glow,
        brush = Brush.radialGradient(
            colors = listOf(Color(0xAAB92D22), Color(0x5567130F), Color.Transparent),
            center = Offset(x + w * 0.30f, y + h * 0.37f),
            radius = w * 0.090f
        ),
        blendMode = BlendMode.Screen
    )

    // ── Glass highlight reflections ────────────────────────────────────────────
    drawLine(
        color = Color.White.copy(alpha = 0.22f),
        start = Offset(x + w * 0.120f, y + h * 0.293f),
        end   = Offset(x + w * 0.360f, y + h * 0.272f),
        strokeWidth = size.minDimension * 0.0020f,
        cap   = StrokeCap.Round
    )
    drawLine(
        color = Color.White.copy(alpha = 0.09f),
        start = Offset(x + w * 0.115f, y + h * 0.380f),
        end   = Offset(x + w * 0.370f, y + h * 0.360f),
        strokeWidth = size.minDimension * 0.0012f,
        cap   = StrokeCap.Round
    )
}

// ─── 9. Top Vertical Fin ──────────────────────────────────────────────────────
private fun DrawScope.drawTopFin(x: Float, y: Float, w: Float, h: Float) {
    val fin = Path().apply {
        moveTo(x + w * 0.740f, y + h * 0.120f)   // fin base-left (on top deck)
        lineTo(x + w * 0.774f, y - h * 0.060f)   // fin tip (rises above hull)
        lineTo(x + w * 0.860f, y + h * 0.008f)   // swept-back tip
        lineTo(x + w * 0.880f, y + h * 0.120f)   // fin base-right (on top deck)
        close()
    }
    drawPath(
        fin,
        brush = Brush.linearGradient(
            colors = listOf(ArmorMid, HullHi, HullDark),
            start  = Offset(x + w * 0.744f, y - h * 0.060f),
            end    = Offset(x + w * 0.880f, y + h * 0.120f)
        )
    )
    // Red accent stripe on fin (matches reference)
    drawLine(
        color = AccentRed.copy(alpha = 0.72f),
        start = Offset(x + w * 0.785f, y - h * 0.028f),
        end   = Offset(x + w * 0.860f, y + h * 0.082f),
        strokeWidth = size.minDimension * 0.0030f,
        cap   = StrokeCap.Round
    )
}

// ─── 10. Engine Nacelle ────────────────────────────────────────────────────────
private fun DrawScope.drawEngineNacelle(x: Float, y: Float, w: Float, h: Float) {
    // Cylindrical engine body (drawn as a shaded oval)
    drawOval(
        brush = Brush.linearGradient(
            colors = listOf(HullHi, ArmorMid, HullMid, HullDark, HullBlack),
            start  = Offset(x + w * 0.830f, y + h * 0.200f),
            end    = Offset(x + w * 0.970f, y + h * 0.650f)
        ),
        topLeft = Offset(x + w * 0.830f, y + h * 0.200f),
        size    = Size(w * 0.120f, h * 0.420f)
    )
    // Barrel ring details
    listOf(0.28f, 0.50f, 0.72f).forEach { t ->
        drawLine(
            color = Color.White.copy(alpha = 0.09f),
            start = Offset(x + w * 0.836f, y + h * (0.200f + 0.420f * t)),
            end   = Offset(x + w * 0.944f, y + h * (0.200f + 0.420f * t)),
            strokeWidth = size.minDimension * 0.0010f
        )
    }
    // Glowing nozzle ring (red)
    drawCircle(
        color  = AccentRed.copy(alpha = 0.90f),
        radius = size.minDimension * 0.036f,
        center = Offset(x + w * 0.866f, y + h * 0.510f),
        style  = Stroke(width = size.minDimension * 0.0068f)
    )
    // Nozzle inner dark circle
    drawCircle(
        color  = HullBlack,
        radius = size.minDimension * 0.022f,
        center = Offset(x + w * 0.866f, y + h * 0.510f)
    )
}

// ─── 11. Front Grille (red weapon / vent block on chin of front face) ─────────
private fun DrawScope.drawFrontGrille(x: Float, y: Float, w: Float, h: Float) {
    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(AccentRed.copy(alpha = 0.96f), AccentRedDeep),
            start  = Offset(x + w * 0.035f, y + h * 0.60f),
            end    = Offset(x + w * 0.115f, y + h * 0.74f)
        ),
        topLeft      = Offset(x + w * 0.035f, y + h * 0.600f),
        size         = Size(w * 0.060f, h * 0.105f),
        cornerRadius = CornerRadius(size.minDimension * 0.0022f, size.minDimension * 0.0022f)
    )
    // Grille slit highlights
    repeat(3) { i ->
        val lineY = y + h * (0.620f + i * 0.025f)
        drawLine(
            color = Color.White.copy(alpha = 0.22f),
            start = Offset(x + w * 0.042f, lineY),
            end   = Offset(x + w * 0.086f, lineY),
            strokeWidth = size.minDimension * 0.0015f,
            cap   = StrokeCap.Round
        )
    }
}

// ─── 12. Front Gun Barrels ────────────────────────────────────────────────────
//  Twin barrels extend forward-left from the nose face — they project TOWARD
//  the viewer in the 3/4 view (appear slightly foreshortened).
private fun DrawScope.drawFrontGunBarrels(x: Float, y: Float, w: Float, h: Float) {
    // Upper barrel
    drawLine(
        color = HullMid,
        start = Offset(x + w * 0.060f, y + h * 0.340f),
        end   = Offset(x - w * 0.024f, y + h * 0.280f),
        strokeWidth = size.minDimension * 0.0048f,
        cap   = StrokeCap.Round
    )
    // Lower barrel
    drawLine(
        color = HullMid,
        start = Offset(x + w * 0.060f, y + h * 0.640f),
        end   = Offset(x - w * 0.024f, y + h * 0.700f),
        strokeWidth = size.minDimension * 0.0048f,
        cap   = StrokeCap.Round
    )
    // Red weapon-tip glow dots
    drawCircle(
        color  = AccentRed.copy(alpha = 0.92f),
        radius = size.minDimension * 0.0090f,
        center = Offset(x - w * 0.022f, y + h * 0.280f)
    )
    drawCircle(
        color  = AccentRed.copy(alpha = 0.92f),
        radius = size.minDimension * 0.0090f,
        center = Offset(x - w * 0.022f, y + h * 0.700f)
    )
}

// ─── 13. Panel Details ────────────────────────────────────────────────────────
private fun DrawScope.drawPanelDetails(x: Float, y: Float, w: Float, h: Float) {
    // Main longitudinal hull seam (runs front-to-rear, converging in perspective)
    drawLine(
        color = Color.White.copy(alpha = 0.08f),
        start = Offset(x + w * 0.265f, y + h * 0.460f),
        end   = Offset(x + w * 0.840f, y + h * 0.390f),
        strokeWidth = size.minDimension * 0.0011f
    )
    // Upper hull seam
    drawLine(
        color = Color.White.copy(alpha = 0.07f),
        start = Offset(x + w * 0.268f, y + h * 0.290f),
        end   = Offset(x + w * 0.820f, y + h * 0.228f),
        strokeWidth = size.minDimension * 0.0010f
    )
    // Vertical divider panel
    drawLine(
        color = Color.White.copy(alpha = 0.07f),
        start = Offset(x + w * 0.450f, y + h * 0.248f),
        end   = Offset(x + w * 0.464f, y + h * 0.654f),
        strokeWidth = size.minDimension * 0.0010f
    )
    // Rear panel box
    drawRoundRect(
        color        = Color(0xFF1A232B),
        topLeft      = Offset(x + w * 0.680f, y + h * 0.274f),
        size         = Size(w * 0.050f, h * 0.066f),
        cornerRadius = CornerRadius(size.minDimension * 0.0018f, size.minDimension * 0.0018f)
    )
    // Tiny rivet dots
    listOf(Pair(0.562f, 0.580f), Pair(0.602f, 0.572f), Pair(0.640f, 0.562f)).forEach { (rx, ry) ->
        drawCircle(
            color  = Color(0x44FFFFFF),
            radius = size.minDimension * 0.0026f,
            center = Offset(x + w * rx, y + h * ry)
        )
    }
    // Small status light on front face
    drawCircle(
        color  = AccentRed.copy(alpha = 0.80f),
        radius = size.minDimension * 0.0060f,
        center = Offset(x + w * 0.164f, y + h * 0.664f)
    )
}

// ─── 14. Engine Exhaust Flame ─────────────────────────────────────────────────
//  Painted last so it always renders on top of the nacelle rim.
private fun DrawScope.drawEngineFlame(x: Float, y: Float, w: Float, h: Float) {
    val flame = Path().apply {
        moveTo(x + w * 0.940f, y + h * 0.400f)
        lineTo(x + w * 1.030f, y + h * 0.344f)
        lineTo(x + w * 1.078f, y + h * 0.460f)
        lineTo(x + w * 1.030f, y + h * 0.576f)
        lineTo(x + w * 0.940f, y + h * 0.530f)
        close()
    }
    drawPath(
        flame,
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFF8A55),
                AccentRed,
                AccentRedDeep,
                Color.Transparent
            ),
            center = Offset(x + w * 1.032f, y + h * 0.460f),
            radius = w * 0.100f
        ),
        blendMode = BlendMode.Screen
    )
}