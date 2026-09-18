package com.vimla.myapplication.Screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import com.vimla.myapplication.R
import com.vimla.myapplication.ui.theme.JadeFreshBackground
import com.vimla.myapplication.ui.theme.JadeFreshBorder
import com.vimla.myapplication.ui.theme.JadeFreshError
import com.vimla.myapplication.ui.theme.JadeFreshOnBackground
import com.vimla.myapplication.ui.theme.JadeFreshOnSurfaceVariant
import com.vimla.myapplication.ui.theme.JadeFreshPrimary
import com.vimla.myapplication.ui.theme.JadeFreshSurface
import com.vimla.myapplication.ui.theme.JadeFreshSurfaceVariant
import com.vimla.myapplication.ui.theme.JadeFreshTertiary
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// ═════════════════════════════════════════════════════════════════════════════
//  ProductCard — "Open Caption" layout. UI SANDBOX COPY.
//
//  Built from the pen sketch: the card holds ONLY the photograph and the
//  quantity/action row. Price, discount and name sit outside it, directly on
//  the page.
//
//      ┌─────────────────────────┐
//      │                     ♡   │
//      │        photograph       │   ← card
//      │                         │
//      │░░500 ml░░░░░░░[  ADD  ]░│   ← tinted shelf, full height, one colour
//      └─────────────────────────┘
//         ₹250  ₹350                 ← outside the card, on the page
//         13% OFF
//         Testing Product
//
//  ── WHY THIS IS BETTER THAN THE STRADDLE VERSION ───────────────────────────
//  It costs about 9dp more height (196dp vs 187dp on a 109dp compact card)
//  because it gives up the straddle's 16dp overlap. In exchange the text block
//  gains 16dp of WIDTH — 109dp instead of 93dp — because it is no longer inside
//  the card's horizontal padding.
//
//  That is a good trade. Row width has been the recurring failure on this card:
//  the price row's worst case (₹1,299 ₹1,650) is about 78dp, which was 84% of
//  the old 93dp budget and is now 72% of 109dp. Long product names get the same
//  16dp before they ellipsise.
//
//  ── WHAT THE SPLIT ALSO BUYS ───────────────────────────────────────────────
//  Chrome now wraps only the part that is an object you can act on. The border,
//  the radius and the elevation say "this is a thing" about the photograph and
//  its controls; the words below are just words, on the page, in the page's own
//  background. Less furniture, and the grid reads lighter for it.
//
//  ── THE RULE THE LAYOUT STILL DEPENDS ON ───────────────────────────────────
//  ONE FLEXIBLE ELEMENT PER ROW; EVERYTHING ELSE A HARD WIDTH. The quantity
//  takes weight(1f) and ellipsises; the control beside it is always a stated
//  width, never a measured one. That is what stops "1 L (approx)" pushing the
//  control off the row.
//
//  The control's width is now allowed to DIFFER between its two states —
//  addSlotWidth when the product isn't in the cart, the wider
//  stepperSlotWidth when it is — and it animates between them. That was tried
//  once before and reverted, on the theory that animating the slot forced the
//  quantity label to re-run its ellipsis measurement every frame and that
//  this was what made the transition feel laggy. That diagnosis was wrong.
//  Re-laying out six characters of "500 ml" costs microseconds; it was never
//  the thing anyone could see.
//
//  The real cost was that ADD and the stepper were each their OWN Surface
//  (their own shadow, their own clip), so AnimatedContent was crossfading two
//  SURFACES rather than their content — two independent shadow/clip layers
//  compositing over each other for the length of every tap, which reads as a
//  flicker rather than a fade. CartAction now opens exactly ONE Surface for
//  the slot, and the two branches are plain content inside it (a Text, or a
//  Row) that share it rather than replace it. That is the mechanism behind
//  Vindusha's ProductCard (FullWidthCartAction) reading as smooth: one
//  persistent container, content-only crossfade. With the expensive part
//  gone, an animated width is affordable — it is one remeasure per frame of a
//  Row with two children.
//
//  Price and struck MRP are still fixed-width and never flex, for the same
//  original reason as the control.
//
//  ── PORTING THIS BACK TO PRODUCTION ────────────────────────────────────────
//  Exactly two things differ from the production file, both marked "PORT:":
//    · ProductImage()          → swap the placeholder for the Coil block
//    · SandboxOutOfStockDialog → swap for the real OutOfStockDialog component
//  Everything else — the profile, the three availability states, the stepper,
//  the semantics — transfers unchanged.
// ═════════════════════════════════════════════════════════════════════════════

// ── File-level shape / colour constants ──────────────────────────────────────
// No ProductMinTouchTarget constant. The row's action slot — NOTIFY's
// RowActionButton, and the shared Surface CartAction opens for ADD/the
// stepper — is drawn at 48dp+ on both axes because the box it fills IS
// actionButtonHeight tall (see CardSizeProfile), so it clears the touch
// minimum by its own visual size, no padding trick needed. The heart is the
// exception:
// it's a deliberately small 28dp puck, so it claims its 48dp through
// minimumInteractiveComponentSize() instead, which expands the TOUCH area
// without expanding the drawn control.
private val ProductCardBorderColor = JadeFreshBorder.copy(alpha = 0.40f)

// The shelf's faint tint. It used to cover only PART of the action area — the
// control's own stack (top gap + control + bottom clearance) was taller than
// the tint, so the control's lower third crossed the tint's edge onto the
// card's own plain white. That read as an unexplained white patch, not a
// design, because the card's body is ALSO white: there was nothing
// distinguishing "off the tinted shelf" from "no shelf at all", just a colour
// that quietly stopped partway down. The tint now covers the WHOLE action
// area — see ProductCard — so there is no edge left for the control to cross.
private val ProductActionBandColor = JadeFreshSurfaceVariant.copy(alpha = 0.30f)

// Full-strength JadeFreshOnSurfaceVariant, not the old 74%-alpha version.
// Struck MRP sits directly on JadeFreshBackground — this row is on the page,
// not on a white card — and at 74% alpha it measured 3.5:1 there: legible on
// a bright phone screen, short of the 4.5:1 small-text guideline everywhere
// else. Dropping the alpha alone clears it (6.71:1). It still reads as
// secondary because of the strikethrough and its smaller size next to the
// price, not because of transparency — so nothing about the hierarchy changes,
// only its legibility.
private val ProductMutedPriceColor = JadeFreshOnSurfaceVariant
private val ProductWishlistBorderColor = JadeFreshBorder.copy(alpha = 0.30f)

// ── WHY NO ROW CONTROL IS EVER TRANSLUCENT ───────────────────────────────────
// NOTIFY once had a translucent tinted fill. That was fine on a white row and
// fell apart the moment the shelf got its own tint — a 10%-alpha jade fill
// composites over a mint shelf into muddy grey-green, and the shadow renders
// THROUGH it as a slab instead of under it as a lift.
//
// So every control on this row is opaque: the cart action is solid jade with
// white text (CartAction), NOTIFY is a white pill with a full-strength accent
// border and accent text (RowActionButton). NOTIFY's accent no longer changes
// with the reason it's showing — see ProductNotifyAccentColor below for why
// that changed.
//
// ── STATUS COLOUR, AUDITED ───────────────────────────────────────────────────
// Two separate decisions live in the constants below, and they used to be
// tangled into one: which colour marks WHY a product is unavailable (the pill
// in PriceOrStatusSlot), and which colour NOTIFY uses to invite the customer
// to act on it. Conflating them is what produced a red "Out of stock" pill
// sitting above a GREEN "NOTIFY" button, and a "Shop closed" pill in a colour
// that failed contrast outright.
//
// Contrast, measured where these pills actually render — JadeFreshBackground,
// not white; this row sits on the page, not on a card:
//     JadeFreshWarning (the old Shop Closed colour) on its own 10% tint
//         -> 1.89:1   (WCAG AA needs 4.5:1 for text this small)
//     JadeFreshError (Out of Stock, unchanged below) on its own 10% tint
//         -> 4.12:1   (passes; kept)
//     the new Shop Closed colour on its own 10% tint
//         -> 4.69:1   (passes)
// 1.89:1 is not a stylistic miss, it is most of the way to invisible — that
// is what "hard to read" measures as.
private val ProductOutOfStockAccentColor = JadeFreshError
private val ProductOutOfStockBackgroundColor = ProductOutOfStockAccentColor.copy(alpha = 0.10f)

// A bespoke shade, not JadeFreshWarning. This needed to go darker than any
// existing warning/amber token to clear contrast on THIS background, and
// retuning the shared theme token would have changed every other screen that
// reads "warning" as a bright, urgent amber — a low-stock strip on a white
// card, for instance, where the brighter version is fine and was never the
// complaint. Still unmistakably the same family as before: warm, not red, not
// green, "this resolves on its own" — just dark enough to actually read.
private val ProductShopClosedAccentColor = Color(0xFF8F5216)
private val ProductShopClosedBackgroundColor = ProductShopClosedAccentColor.copy(alpha = 0.10f)

// NOTIFY's colour, in BOTH blocked states — the one constant here that is NOT
// a "what does this status mean" colour. The pill above it already says why
// the product is blocked; the button's job is to invite a follow-up action,
// not repeat the warning. One calm colour for that action regardless of the
// reason is what removes the mismatch. Deliberately not JadeFreshPrimary
// either, which is ADD's colour elsewhere in the same grid — a customer
// scanning past a row of cards should never have to wonder whether NOTIFY and
// ADD are the same action wearing two different states.
private val ProductNotifyAccentColor = JadeFreshTertiary

// The discount line's colour. JadeFreshSuccess (an alias for JadeFreshPrimary)
// measures 3.92:1 directly on JadeFreshBackground — this text has no white
// card behind it either — so "good news" text was, technically, failing
// contrast too. JadeFreshTertiary is the same brand green pushed darker,
// which is what clears it (6.46:1); see PriceRow, where this now renders.
private val ProductDiscountColor = JadeFreshTertiary

// The vendor badge, below the price row. Two earlier fills before this one:
// JadeFreshSurfaceVariant (a pale mint chip that read as an almost-
// transparent smudge on the equally pale page), then JadeFreshPremium — a
// mango orange chosen specifically to be bold without repeating the
// reference photo's literal #ffed00, because that yellow measured only
// 1.05:1 shape-contrast against JadeFreshBackground (this app's actual page
// colour): bright yellow and a pale mint page are both very high-luminance,
// so despite looking like obviously different hues in a photo, WCAG contrast
// (which is luminance-only) would have rendered them nearly as washed out
// against each other as the first chip was.
//
// This is the literal #ffed00 now, by explicit choice after that trade-off
// was on the table — and the honest consequence of it is the border two
// lines down, which is NOT decorative here the way it would be on a bolder
// fill: at 1.05:1 the yellow alone doesn't reliably read as a rectangle
// against this specific page, so something has to draw its edge. The text
// stays JadeFreshOnBackground — unaffected by the fill choice, and better
// for it: 13.93:1 on yellow, up from 5.87:1 on the mango orange, the
// highest-contrast pairing this badge has had.
private val ProductVendorBadgeAccentColor = JadeFreshOnBackground
private val ProductVendorBadgeBackgroundColor = Color(0xFFFFED00)
// The edge #ffed00 needs that JadeFreshPremium didn't — see above. Kept in
// the text's own colour at low alpha rather than a new hue, so the badge
// reads as "one warm graphic" (fill, edge, and text all related) instead of
// a yellow rectangle with an arbitrary outline colour.
private val ProductVendorBadgeBorderColor = ProductVendorBadgeAccentColor.copy(alpha = 0.30f)

// ─────────────────────────────────────────────────────────────────────────────
// The three ways a visible product can present. Mirrors production exactly.
// Precedence, when more than one could apply: ShopClosed > OutOfStock > Purchasable.
// ─────────────────────────────────────────────────────────────────────────────
enum class ProductAvailabilityState { Purchasable, OutOfStock, ShopClosed }

// ─────────────────────────────────────────────────────────────────────────────
// Data model. Same shape as production, minus the fields only the vendor gate
// uses. `tint` and `emoji` remain sandbox-only stand-ins; `imageRes` is not —
// see its own comment below.
// ─────────────────────────────────────────────────────────────────────────────
data class ProductCardData(
    val id: String,
    val name: String,
    val qty: String,
    val price: Double,
    val mrp: Double,
    val availableStock: Int = 10,
    val vendorName: String = "",
    val gatedState: ProductAvailabilityState? = null,

    // A real bundled drawable, when the caller has one. Same field name and
    // type as Vindusha's production ProductCardData, deliberately — this is
    // the one field here that is not a stand-in, just an earlier stage of the
    // real thing: a local resource today, a Coil-loaded imageUrl once this
    // ports back (see ProductImage). Null falls back to the emoji/tint pair
    // below, so every sample product that predates this still renders.
    val imageRes: Int? = null,

    // The vendor's own photo/logo, for the circular avatar on VendorBadge.
    // Same shape as imageRes, same reasoning — a real resource when there is
    // one, and VendorAvatar falls back to a coloured initial (the vendor
    // name's first letter) when there isn't. None of the sample vendors
    // below have a drawable for this yet — there's no vendor-logo asset in
    // res/drawable the way there is for products — so every sample renders
    // the initial fallback today; the real-photo path exists and is ready
    // the moment one does.
    val vendorImageRes: Int? = null,

    // PORT: sandbox only, and now only the FALLBACK for products with no
    // imageRes. Production reads imageUrl / imageRes instead.
    val emoji: String = "🛒",
    val tint: Int = 0
) {
    val discountPercent: Int
        get() = if (mrp > 0.0) (((mrp - price) / mrp) * 100).toInt() else 0

    val displayPrice: String get() = price.toInt().toString()
    val displayMrp: String get() = mrp.toInt().toString()

    val availabilityState: ProductAvailabilityState
        get() = gatedState ?: if (availableStock <= 0) {
            ProductAvailabilityState.OutOfStock
        } else {
            ProductAvailabilityState.Purchasable
        }

    val isPurchasable: Boolean get() = availabilityState == ProductAvailabilityState.Purchasable
}

// ── Text styles ──────────────────────────────────────────────────────────────
@Composable
private fun productNameTextStyle(sp: Float): TextStyle = MaterialTheme.typography.bodyMedium.copy(
    // Bold, not SemiBold — a title needs that extra weight to hold its own
    // sitting right under an ExtraBold price line, or it reads as an
    // afterthought beneath the number.
    fontSize = sp.sp, fontWeight = FontWeight.Bold, lineHeight = (sp * 1.3f).sp
)

@Composable
private fun productQtyTextStyle(sp: Float): TextStyle = MaterialTheme.typography.labelMedium.copy(
    fontSize = sp.sp, fontWeight = FontWeight.Bold
)

@Composable
private fun productPriceTextStyle(sp: Float): TextStyle = MaterialTheme.typography.titleMedium.copy(
    fontSize = sp.sp, fontWeight = FontWeight.ExtraBold, lineHeight = (sp * 1.1f).sp
)

@Composable
private fun productMrpTextStyle(sp: Float): TextStyle = MaterialTheme.typography.bodySmall.copy(
    // lineHeight scaled with fontSize — this was the other style in the file
    // (besides productStatusTextStyle, fixed earlier) that silently kept
    // bodySmall's fixed 16sp line box regardless of the smaller sp actually
    // requested. Paired in the same row with productPriceTextStyle's own
    // tight 1.1x line height, that mismatch is what made the struck price
    // look vertically lifted off the selling price. alignByBaseline() in
    // PriceRow is the actual fix for that — this just stops the style itself
    // from lying about the size it renders at.
    fontSize = sp.sp, textDecoration = TextDecoration.LineThrough, lineHeight = (sp * 1.1f).sp
)

@Composable
private fun productDetailTextStyle(sp: Float): TextStyle = MaterialTheme.typography.labelSmall.copy(
    fontSize = sp.sp, fontWeight = FontWeight.Bold, lineHeight = (sp * 1.3f).sp
)

// A separate style from productDetailTextStyle, not a reuse of it, even
// though the vendor badge used to just borrow that one. ExtraBold here,
// specifically — one step heavier than the discount text's plain Bold —
// because a solid-colour highlight block reads as confident precisely when
// the text sitting on it is as heavy as the block itself; a merely-Bold
// label on a bold fill looks like it's apologising for being there.
@Composable
private fun productVendorTextStyle(sp: Float): TextStyle = MaterialTheme.typography.labelSmall.copy(
    fontSize = sp.sp, fontWeight = FontWeight.ExtraBold, lineHeight = (sp * 1.3f).sp
)

// No explicit lineHeight override — deliberately, unlike every other style
// in this file. The other styles all needed one because their box sat
// beside or inside something whose alignment depended on matching box
// heights (see productMrpTextStyle and productStatusTextStyle's own doc
// comments for the two times getting that wrong caused a visible bug). This
// single glyph sits centred inside its own circle with nothing beside it to
// misalign against, so there's no box-height contract to keep, and adding
// one would just be a number with no job.
@Composable
private fun productVendorAvatarInitialTextStyle(sp: Float): TextStyle =
    MaterialTheme.typography.labelSmall.copy(fontSize = sp.sp, fontWeight = FontWeight.Bold)

@Composable
private fun productStatusTextStyle(sp: Float): TextStyle = MaterialTheme.typography.bodyMedium.copy(
    // lineHeight scaled with fontSize, like every other style in this
    // file — this one alone used to skip it, silently inheriting
    // bodyMedium's default 20sp line box no matter what fontSize was
    // passed. At statusSp=10f that meant the pill's own text wanted a
    // 20dp-tall line by itself, which is MORE than the entire 18dp box
    // StatusPill used to render inside — the pill wasn't badly styled,
    // it was being compressed by roughly a third. See StatusRow for the
    // other half of this fix.
    fontSize = sp.sp, fontWeight = FontWeight.SemiBold, lineHeight = (sp * 1.3f).sp
)

@Composable
private fun productActionTextStyle(sp: Float): TextStyle = MaterialTheme.typography.labelLarge.copy(
    fontSize = sp.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (sp * 0.06f).sp
)

@Composable
private fun productStepperCountTextStyle(sp: Float): TextStyle =
    MaterialTheme.typography.titleMedium.copy(fontSize = sp.sp, fontWeight = FontWeight.ExtraBold)

@Composable
private fun productStepperSymbolTextStyle(sp: Float): TextStyle =
    MaterialTheme.typography.titleLarge.copy(fontSize = sp.sp, fontWeight = FontWeight.Bold)

// ─────────────────────────────────────────────────────────────────────────────
// CardSizeProfile — every size-sensitive token in one place, so no
// sub-composable duplicates breakpoint logic.
//
// isCompact = true  when cardWidth < 140dp  (3-col grid: ~109dp)
// isCompact = false when cardWidth ≥ 140dp  (2-col grid / home rail: ~168dp)
// ─────────────────────────────────────────────────────────────────────────────
private data class CardSizeProfile(
    val isCompact: Boolean,
    val cardRadius: Dp,

    // ── The action area, inside the card, below the photograph ───────────
    // Its height is defined from the CONTROL outwards, not from a band
    // inwards, because the control's vertical position is the thing that kept
    // going wrong:
    //
    //     photograph
    //     ├─ actionButtonTopGap           clear air, so the control can never
    //     │                               bite into the image again
    //     ├─ actionButtonHeight           the control itself
    //     └─ actionButtonBottomClearance  so it never lands on the card's edge
    //
    // The area measures exactly that sum. Both gaps being TERMS IN THE HEIGHT
    // is the whole point: they cannot be squeezed out by anything, whereas
    // the old geometry produced the control's position as a side effect of
    // centring a 48dp control inside a 30dp row, which overflowed it by 9dp
    // in both directions — 9dp up onto the photograph, 9dp down into the
    // card's bottom border, leaving 1dp of clearance.
    val actionButtonTopGap: Dp,
    val actionButtonBottomClearance: Dp,
    val actionRowPaddingH: Dp,
    val actionRowGap: Dp,

    // The ADD / NOTIFY control's height, and — since the box holding it is
    // exactly this tall — the height of the row itself. The control claims it
    // with a plain fillMaxHeight(), and the quantity label beside it is
    // centred on that same box, which is what makes the two read as aligned
    // with each other.
    //
    // This used to be applied with requiredHeight() so the control could be
    // taller than its row and overflow it. Nothing needs that now: the row is
    // exactly the control's own size, and the tint behind it now runs the
    // full action area — see ProductActionBandColor — so there is no shorter
    // "shelf" left for anything to be measured against either.
    val actionButtonHeight: Dp,
    // ── The control widths ───────────────────────────────────────────────
    // The row is one flexible element (the quantity, weight(1f)) plus one
    // stated width, so these numbers ARE the quantity's budget — whatever
    // they don't take, it gets:
    //
    //   compact card, 97dp for label + control
    //     idle    (ADD / NOTIFY)  60dp -> quantity gets 37dp
    //     in cart (stepper)       82dp -> quantity gets 15dp
    //
    // The 15dp is deliberate and is the point of the design, not a casualty
    // of it: once the product is in the cart the stepper is what the customer
    // is aiming at, and the quantity — which they have already read — steps
    // back to a leading character and an ellipsis, the same way "1 L
    // (approx)" has always had to. QuantityLabel is maxLines = 1 with
    // TextOverflow.Ellipsis, so this degrades by truncating, never by
    // wrapping, pushing the control off the row, or overlapping anything:
    // Row hands the control its stated width first and the label takes what
    // remains, so the two can't collide however tight it gets.
    //
    // stepperSlotWidth is the number to watch when tuning. Its practical
    // ceiling is where the label can no longer show one character plus the
    // ellipsis glyph (about 11dp at 10sp) — past that the quantity stops
    // saying anything at all, and at that point it would be more honest to
    // drop it than to render a lone "…".
    val addSlotWidth: Dp,
    val stepperSlotWidth: Dp,
    // NOTIFY's own. It never animates and never shares a row with the cart
    // control — the two states are mutually exclusive — but it matches
    // addSlotWidth so that a grid mixing available and unavailable products
    // has one consistent idle control width down the column.
    val notifySlotWidth: Dp,
    val actionButtonRadius: Dp,
    val actionButtonBorder: Dp,
    val actionPaddingH: Dp,

    // Below the card, on the page
    val cardToPrice: Dp,
    val priceToDetail: Dp,
    val detailToName: Dp,
    val priceSlotHeight: Dp,
    // Sized for the vendor badge now, not the bare "13% OFF" text this slot
    // used to hold — a padded Surface needs more room than plain text at the
    // same font size did, and now it holds a circular avatar too, which is
    // taller than a single text line is on its own. Content height is
    // max(text line, vendorAvatarDp) + pillPaddingV top and bottom: ~20dp
    // compact / ~26dp standard at the current sizes. This leaves roughly 1dp
    // of slack over that rather than sizing to the exact minimum, on the
    // theory that this file has now shipped a slot sized to precisely what a
    // component needed and precisely nothing more often enough times (see
    // productStatusTextStyle and productMrpTextStyle's own doc comments) to
    // treat "exactly enough" as a recurring failure mode worth a margin
    // against, not a one-off.
    val detailSlotHeight: Dp,

    // Text sizes
    val nameSp: Float,
    val qtySp: Float,
    val priceSp: Float,
    val mrpSp: Float,
    val detailSp: Float,
    val statusSp: Float,
    val actionSp: Float,
    val stepperSymbolSp: Float,
    val stepperCountSp: Float,

    val statusIconDp: Dp,
    val pillPaddingH: Dp,
    val pillPaddingV: Dp,
    val pillIconGap: Dp,
    val heartPadding: Dp,
    val emojiSp: Float,

    // VendorBadge's circular avatar — the vendor's own photo when there is
    // one, a coloured initial when there isn't. Sized independently of
    // statusIconDp: StatusPill's icon sits beside a taller line of text
    // (statusSp) and only has to not look tiny next to it, while this circle
    // is close to the tallest thing in its own row and directly sets how
    // much detailSlotHeight above needs to grow — a size chosen without
    // checking that arithmetic is exactly how this slot got shipped too
    // short twice already.
    val vendorAvatarDp: Dp,
    val vendorAvatarInitialSp: Float,
)

private fun cardSizeProfile(cardWidth: Dp): CardSizeProfile {
    val compact = cardWidth < 140.dp
    return if (compact) CardSizeProfile(
        isCompact = true,
        cardRadius = 14.dp,
        // 3dp of air under the photograph. Small, but it is the entire
        // difference between a control that sits below the image and one
        // that sits ON it.
        actionButtonTopGap = 3.dp,
        actionButtonBottomClearance = 4.dp,
        // Tightened again, 5dp -> 4dp. Every dp here comes straight off the
        // quantity's width budget, and the quantity has to survive alongside
        // the STEPPER, the wider of the two controls this row holds.
        actionRowPaddingH = 4.dp,
        actionRowGap = 4.dp,
        // 48dp is Android's own minimum touch target, cleared by the control's
        // drawn size alone on both axes (48 × 60dp idle, 48 × 82dp in the
        // cart) with no padding trick.
        actionButtonHeight = 48.dp,
        // 60dp idle. Comfortable for "ADD" (~23dp at 11sp ExtraBold) and
        // enough for "NOTIFY" (~41dp) — but only because actionPaddingH came
        // down to 4dp with it; see that field.
        addSlotWidth = 60.dp,
        // 82dp in the cart: +22dp, so the − and + cells go from 27.5dp each
        // (they were ~23dp before this) and the control visibly opens up on
        // the tap. The quantity is left 15dp, which is a leading character
        // and an ellipsis — the intended trade, see the field's doc comment.
        stepperSlotWidth = 82.dp,
        // Matched to addSlotWidth so every idle card in a grid presents the
        // same control width, whether the product is buyable or not.
        notifySlotWidth = 60.dp,
        actionButtonRadius = 11.dp,
        actionButtonBorder = 1.5.dp,
        // 8dp -> 4dp, forced by the 60dp idle slot. This is NOTIFY's inner
        // padding, and "NOTIFY" is ~41dp of glyphs at 11sp ExtraBold: 8dp a
        // side left it 44dp of room, a 3dp margin that the first user with
        // display font scaling turned on would have eaten. 4dp leaves 52dp.
        actionPaddingH = 4.dp,
        cardToPrice = 6.dp,
        priceToDetail = 2.dp,
        detailToName = 4.dp,
        priceSlotHeight = 18.dp,
        // 12dp -> 19dp -> 20dp. First bump was for the badge's padding once
        // it was text-only; this one is for the 14dp avatar circle now
        // sitting in the same row — max(text line, 14dp) + pillPaddingV×2 =
        // ~20dp (see the field's own doc comment on CardSizeProfile).
        detailSlotHeight = 20.dp,
        nameSp = 10.5f,
        // 10sp, not 10.5. The quantity's width budget swings between 37dp
        // idle and 15dp with the stepper up (see addSlotWidth), and at the
        // narrow end half a point decides whether a leading character still
        // renders beside the ellipsis or the label collapses to "…" alone.
        qtySp = 10f,
        priceSp = 13.5f,
        mrpSp = 10f,
        detailSp = 9f,
        // 10sp is what fits "Out of stock" in the width a compact card has.
        statusSp = 10f,
        // Raised from 9.5. In the reference the button label is about 6% of the
        // card's width in cap height, which is a big, confident word — that
        // proportion is most of why the button reads as the primary action.
        actionSp = 11f,
        stepperSymbolSp = 15f,
        stepperCountSp = 12f,
        statusIconDp = 11.dp,
        pillPaddingH = 5.dp,
        pillPaddingV = 3.dp,
        pillIconGap = 4.dp,
        heartPadding = 5.dp,
        emojiSp = 34f,
        // 14dp — bigger than statusIconDp's 11dp on purpose. A circular photo
        // avatar reads as a recognisable "this is a small picture" shape at
        // 14dp in a way it wouldn't much smaller; a plain line icon like
        // StatusPill's doesn't carry that same lower bound.
        vendorAvatarDp = 14.dp,
        // Roughly half the circle's own diameter, which is what keeps a
        // single letter looking centred and proportionate rather than
        // swimming in the middle of its circle or crowding its edge.
        vendorAvatarInitialSp = 8f,
    ) else CardSizeProfile(
        isCompact = false,
        cardRadius = 16.dp,
        actionButtonTopGap = 4.dp,
        actionButtonBottomClearance = 5.dp,
        actionRowPaddingH = 8.dp,
        actionRowGap = 8.dp,
        actionButtonHeight = 60.dp,
        // The compact profile's proportions carried up to a 168dp card, so
        // both breakpoints animate the same way: idle takes ~61% of the row's
        // 144dp, the stepper ~81%. That leaves the quantity 56dp idle and
        // 28dp in the cart — the wider card buys the label about two
        // characters more before the ellipsis than a compact one does.
        addSlotWidth = 88.dp,
        stepperSlotWidth = 116.dp,
        notifySlotWidth = 88.dp,
        actionButtonRadius = 13.dp,
        actionButtonBorder = 1.5.dp,
        actionPaddingH = 14.dp,
        cardToPrice = 8.dp,
        priceToDetail = 3.dp,
        detailToName = 5.dp,
        priceSlotHeight = 24.dp,
        // 15dp -> 24dp -> 26dp, same two-step reasoning as the compact
        // profile: max(text line, 18dp avatar) + pillPaddingV×2 = ~26dp.
        detailSlotHeight = 26.dp,
        nameSp = 13f,
        qtySp = 13f,
        priceSp = 19f,
        mrpSp = 12f,
        detailSp = 11f,
        statusSp = 13f,
        actionSp = 14f,
        stepperSymbolSp = 18f,
        stepperCountSp = 15f,
        statusIconDp = 14.dp,
        pillPaddingH = 8.dp,
        pillPaddingV = 4.dp,
        pillIconGap = 6.dp,
        heartPadding = 7.dp,
        emojiSp = 52f,
        vendorAvatarDp = 18.dp,
        vendorAvatarInitialSp = 10f,
    )
}

// ═════════════════════════════════════════════════════════════════════════════
//  ProductCard
//
//  NOTE: the outer element is a Column, NOT a Card. That is the whole point of
//  this layout — only the photograph and the action row are carded. `modifier`
//  therefore applies to the Column, and the Card inside it is not configurable
//  from outside. Callers pass Modifier.fillMaxWidth() exactly as before.
//
//  ONE CLICK REGION, TWO VISUAL PIECES. The photograph+card and the
//  price/name text below it used to each carry their OWN separate
//  .clickable(onClick = onCardClick) — same callback, but two different
//  ripple regions with a gap between them that belonged to neither, so
//  tapping there did nothing and the card read as two things placed near
//  each other rather than one. The single .clickable() now lives on THIS
//  outer Column instead, covering the photograph, the gap, and the text as
//  one continuous region — nothing about how any of it LOOKS changes; only
//  where a tap is heard changes. Inner controls (the heart, ADD, the
//  stepper) keep their own independent .clickable()s and are unaffected:
//  Compose resolves nested clickables by letting the innermost one that
//  consumes a tap win, so tapping the heart still only fires
//  onFavouriteClick, never also onCardClick.
// ═════════════════════════════════════════════════════════════════════════════
@Composable
fun ProductCard(
    product: ProductCardData,
    modifier: Modifier = Modifier.fillMaxWidth(),
    imageHeight: Dp = Dp.Unspecified,
    cardWidth: Dp = 160.dp,
    isWishlisted: Boolean = false,
    cartQuantity: Int = 0,
    onCardClick: () -> Unit = {},
    onFavouriteClick: () -> Unit = {},
    onAddToCartClick: (newQuantity: Int) -> Unit = {},
    onNotifyClick: (message: String) -> Unit = {}
) {
    val p = remember(cardWidth) { cardSizeProfile(cardWidth) }

    // Square unless a caller overrides. The old card defaulted to a hard 162dp,
    // which on a 109dp grid cell was a portrait crop nobody chose.
    val resolvedImageHeight = if (imageHeight.isSpecified) imageHeight else cardWidth

    val heartTint by animateColorAsState(
        targetValue = if (isWishlisted) Color(0xFFE24A4A) else JadeFreshOnSurfaceVariant,
        animationSpec = tween(220),
        label = "heart_${product.id}"
    )

    val cardShape = RoundedCornerShape(p.cardRadius)

    Column(
        modifier = modifier
            // Bounds the ripple to the card's rounded TOP corners only — NOT
            // cardShape, which is wrong here and was the actual cause of the
            // clipped "T"/"F"/"A" bug: cardShape rounds all four corners, and
            // clip() applies that rounding to THIS Column's own bounds, which
            // run from the top of the photograph all the way past the product
            // name at the very bottom. Its bottom-left and bottom-right
            // corners land right where the name starts, and a rounded corner
            // there doesn't just look like a curve — it's a hole cut out of
            // the rectangle, and the name's leading glyph was sitting inside
            // it. topStart/topEnd-only, with the bottom two corners left at
            // their 0dp default, rounds where the photograph actually is and
            // is a perfectly square no-op everywhere else — nothing at the
            // bottom of this Column has anything cut from it.
            .clip(RoundedCornerShape(topStart = p.cardRadius, topEnd = p.cardRadius))
            .semantics {
                stateDescription = when (product.availabilityState) {
                    ProductAvailabilityState.ShopClosed -> "${product.name}, shop is closed"
                    ProductAvailabilityState.OutOfStock -> "${product.name}, out of stock"
                    ProductAvailabilityState.Purchasable -> "${product.name}, in stock"
                }
            }
            .clickable(
                onClickLabel = "Open ${product.name}", role = Role.Button, onClick = onCardClick
            )) {
        // ── THE CARD — photograph + action row, and nothing else ──────────
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = cardShape,
            color = JadeFreshSurface,
            shadowElevation = 1.5.dp,
            border = BorderStroke(1.dp, ProductCardBorderColor)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // ── Photograph ───────────────────────────────────────────
                // No clickable here any more — see the outer Column, above.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(resolvedImageHeight)
                ) {
                    ProductImage(product = product, p = p, cardRadius = p.cardRadius)

                    // No scrim any more. It existed to keep a button legible
                    // where it overlapped the photograph; with the action row
                    // moved fully below the image, darkening the bottom of
                    // every product photo would be decoration with no job.
                    FavouriteOverlayButton(
                        isWishlisted = isWishlisted,
                        heartTint = heartTint,
                        onFavouriteClick = onFavouriteClick,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(p.heartPadding)
                    )
                }

                // No divider here any more — that was the "white separation
                // border" between the photograph and the action band.
                // Removed on request: Vindusha's card has nothing between its
                // image and its info zone either, just the colour change
                // itself. The divider was the one piece of this card's
                // chrome that had no counterpart there, so the photograph now
                // runs straight into the tinted band below.

                // ── ACTION AREA — one tinted shelf, full height, no seam ────
                //
                // This Box's height IS the control's vertical stack: top gap
                // + control + bottom clearance. Stating it that way is what
                // makes the placement bug it replaced unrepeatable. Air above
                // the control and air below it are terms in the height
                // itself, so nothing can squeeze them out — where before, the
                // control's position fell out of centring a 48dp control in a
                // 30dp row, which overflowed 9dp onto the photograph at the
                // top and stopped 1dp short of the card's border at the
                // bottom.
                //
                // The tint is this Box's OWN background now, covering the
                // full stack — not a separate, shorter child the way it used
                // to be. That shorter version was deliberate at the time: the
                // idea was a control standing on a shelf and jutting past its
                // edge onto the card's own colour, like an object with a
                // shadow under it. In practice the card's own colour is
                // white, the same white the tint fades toward at low alpha,
                // so "past the shelf" and "plain card" were nearly
                // indistinguishable — the crossing didn't read as a raised
                // object, it read as an unexplained pale gap under the
                // control. One tint, full height, removes the gap instead of
                // trying to make it look intentional.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            p.actionButtonTopGap + p.actionButtonHeight + p.actionButtonBottomClearance
                        )
                        .background(ProductActionBandColor)
                ) {
                    // The row. Exactly as tall as the control it holds, which
                    // is what puts the quantity label and the control on one
                    // shared centre line — the thing that made them look
                    // misaligned was the label being centred on a 30dp row
                    // while the control was centred on its own 48dp self.
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .padding(
                                top = p.actionButtonTopGap,
                                start = p.actionRowPaddingH,
                                end = p.actionRowPaddingH
                            )
                            .height(p.actionButtonHeight)
                    ) {
                        // A two-way boolean cannot express "unbuyable but in
                        // stock", which is exactly what a closed shop is —
                        // that path would render a working ADD for a shop
                        // that cannot take the order.
                        when (product.availabilityState) {
                            ProductAvailabilityState.ShopClosed -> NotifyRow(
                                qty = product.qty,
                                contentDescription = "Notify vendor",
                                message = if (product.vendorName.isNotBlank()) {
                                    "We'll let ${product.vendorName} know you're waiting!"
                                } else {
                                    "We'll let the vendor know you're waiting!"
                                },
                                onNotifyClick = onNotifyClick,
                                p = p
                            )

                            ProductAvailabilityState.OutOfStock -> NotifyRow(
                                qty = product.qty,
                                contentDescription = "Notify me",
                                message = "We'll notify you when \"${product.name}\" is back in stock!",
                                onNotifyClick = onNotifyClick,
                                p = p
                            )

                            ProductAvailabilityState.Purchasable -> CartAction(
                                product = product,
                                quantity = cartQuantity,
                                onQuantityChange = onAddToCartClick,
                                p = p
                            )
                        }
                    }
                }
            }
        }

        // ── BELOW THE CARD — on the page, no chrome, full column width ────
        // This block gets the whole cardWidth instead of cardWidth minus the
        // card's horizontal padding. On a 109dp compact card that is 16dp more
        // room for the price row and the name, which is the main thing this
        // layout buys. No clickable here either any more — see the outer
        // Column, at the top of this function.
        Spacer(Modifier.height(p.cardToPrice))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Two different amounts of vertical territory need the SAME total
            // height, or the grid goes ragged (a LazyVerticalGrid row sizes to
            // its tallest card) — see StatusRow's doc comment for the exact
            // arithmetic that keeps them equal.
            if (product.isPurchasable) {
                PriceRow(product = product, p = p)
                Spacer(Modifier.height(p.priceToDetail))
                VendorBadge(product = product, p = p)
            } else {
                StatusRow(product = product, p = p)
            }
            Spacer(Modifier.height(p.detailToName))
            Text(
                text = product.name,
                style = productNameTextStyle(p.nameSp),
                color = JadeFreshOnBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * PORT: half of this is no longer the stand-in.
 *
 * A real imageRes renders exactly the way it will in production — clipped to
 * the same top corners, Crop-scaled the same way — via painterResource()
 * rather than Coil. That's deliberate, not a shortcut: these are local
 * bundled drawables, not URLs, and Coil has nothing to do for a resource
 * that's already on-device and already the right file — painterResource() is
 * the plain, synchronous, zero-dependency way Compose reads one. The
 * gradient+emoji block is what remains stand-in, and only renders for a
 * product with no imageRes.
 *
 * In production the emoji branch disappears entirely, and imageRes (or
 * imageUrl, for a remote catalogue photo) both feed the Coil block Vindusha's
 * ProductImage already uses — the canonical decode size, the instant-paint
 * probe, the Loading / Error painter states. Nothing outside this function
 * needs to change when that swap happens.
 */
@Composable
private fun ProductImage(product: ProductCardData, p: CardSizeProfile, cardRadius: Dp) {
    // Top corners only, either way — the bottom of the photograph meets the
    // action shelf, and the card's own clip handles the outer edge.
    val shape = RoundedCornerShape(topStart = cardRadius, topEnd = cardRadius)

    if (product.imageRes != null) {
        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
        )
        return
    }

    val tints = listOf(
        listOf(Color(0xFFDCEBD9), Color(0xFFB9D6B3)),
        listOf(Color(0xFFF7E3DC), Color(0xFFEFC7BA)),
        listOf(Color(0xFFE4EAD6), Color(0xFFCBDAB2)),
        listOf(Color(0xFFDEE8F1), Color(0xFFC0D4E5)),
        listOf(Color(0xFFF8EFD8), Color(0xFFEEDCB2)),
        listOf(Color(0xFFEFE2EE), Color(0xFFDCC5DA))
    )
    val pair = tints[product.tint % tints.size]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(Brush.linearGradient(pair)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = product.emoji, fontSize = p.emojiSp.sp)
    }
}

/**
 * The quantity, as plain text on the action row — a box around it would be a
 * second border inside a card that already has one.
 *
 * Every caller gives it Modifier.weight(1f), and that is load-bearing: it is
 * the ONE flexible element on the row. The control beside it always states its
 * width rather than measuring one, so a long quantity ("1 L (approx)")
 * ellipsises here instead of pushing the control off the card.
 *
 * On the purchasable row that width is not constant — it animates when the
 * product goes into the cart (see CartAction) — so this label's box narrows
 * and widens with it. It ellipsises more of its tail while the stepper is up.
 * That is the intended trade and it is budgeted for: see addSlotWidth on
 * CardSizeProfile for the dp each state actually leaves here.
 */
@Composable
private fun QuantityLabel(qty: String, p: CardSizeProfile, modifier: Modifier = Modifier) {
    Text(
        text = qty,
        style = productQtyTextStyle(p.qtySp),
        color = JadeFreshOnBackground,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * The price line — reachable only when the product is purchasable; ProductCard
 * routes anything else to StatusRow instead. Fixed height so the grid stays
 * even, matching StatusRow's total (see that composable's doc comment).
 *
 * Price and struck MRP both size to their own content — no weight, no flex.
 * Worst case in a grocery catalogue (₹1,299 ₹1,650) is about 78dp, which is
 * 72% of the 109dp this block gets. The discount percentage moved into this
 * row too (was its own line below, see VendorBadge for what replaced it) and
 * does NOT get the same no-weight treatment: "₹1,299 ₹1,650 21% OFF", the
 * same worst case plus its discount, needs roughly 117dp — MORE than a
 * compact card's 109dp has at all. Price and MRP still never flex; they're
 * the numbers a customer is actually reading and must never truncate. The
 * discount is genuinely the least important of the three, so it's the one
 * that carries Modifier.weight(1f) — it gets whatever room price and MRP
 * didn't need, and ellipsises rather than pushing the row past the card's
 * edge on the rare product where all three don't fit together.
 *
 * All three Texts use Modifier.alignByBaseline(), not Row's own
 * verticalAlignment = Alignment.Bottom the way this used to be written.
 * Bottom-aligning BOXES only lines up baselines when both boxes have the
 * identical distance from their own bottom edge to the glyphs inside them —
 * and they didn't: productPriceTextStyle sets a deliberate, tight lineHeight
 * (1.1x its font size), while productMrpTextStyle used to set none at all,
 * silently inheriting bodySmall's fixed 16sp line box regardless of the
 * smaller font size actually requested. Two different box-to-glyph offsets,
 * bottom-aligned, is exactly what "the struck price looks lifted" looks
 * like — the boxes' bottoms matched, the text sitting inside them didn't.
 * alignByBaseline() sidesteps the whole problem: it aligns the actual
 * baselines Compose already computes for each Text, regardless of how tall
 * either box is, which is the correct tool any time text of different sizes
 * needs to sit on one visual line — no lineHeight bookkeeping required, and
 * immune to the next person changing priceSp or mrpSp independently.
 */
@Composable
private fun PriceRow(product: ProductCardData, p: CardSizeProfile) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(p.priceSlotHeight),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "₹${product.displayPrice}",
                style = productPriceTextStyle(p.priceSp),
                color = JadeFreshOnBackground,
                maxLines = 1,
                modifier = Modifier.alignByBaseline()
            )
            if (product.mrp > product.price) {
                Text(
                    text = "₹${product.displayMrp}",
                    style = productMrpTextStyle(p.mrpSp),
                    color = ProductMutedPriceColor,
                    maxLines = 1,
                    modifier = Modifier.alignByBaseline()
                )
            }
            if (product.discountPercent > 0) {
                Text(
                    text = "${product.discountPercent}% OFF",
                    style = productDetailTextStyle(p.detailSp),
                    color = ProductDiscountColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .alignByBaseline()
                )
            }
        }
    }
}

/**
 * The blocked-state pill's row — ShopClosed and OutOfStock only; ProductCard
 * routes Purchasable to PriceRow + VendorBadge instead.
 *
 * THIS is the actual fix for the pill "not fitting": the pill used to render
 * inside a Box sized to priceSlotHeight ALONE (18dp compact), sized for a bare
 * price line — while the slot below it sat right underneath it, EMPTY, still
 * reserving its own height for content a blocked product never shows. The
 * pill was fighting for a fifth of a card's real usable space while the rest
 * of that space sat idle one line below it.
 *
 * This box claims all of it — priceSlotHeight + priceToDetail + detailSlotHeight,
 * the exact same total PriceRow + its spacer + VendorBadge add up to — so the
 * two branches in ProductCard always occupy identical total height (which is
 * what keeps the grid even) while the pill gets the full 39dp/51dp to work
 * with instead of being squeezed into the first 18dp/24dp of it. (That total
 * grew from 32dp/42dp when detailSlotHeight itself grew to fit the vendor
 * badge — see that field's doc comment on CardSizeProfile — and StatusRow's
 * own height followed it automatically, since both read the same field;
 * nothing here had to change by hand for the two branches to stay equal.)
 *
 * That squeeze was compounding a second, independent bug — productStatusTextStyle
 * was quietly inheriting a 20sp line height regardless of the sp it was asked
 * for, so the pill's OWN wanted size (≈26dp compact) already exceeded its old
 * 18dp box before this fix. Both are fixed now: the text style asks for a
 * size that matches what it's actually rendering, and the box it renders into
 * has comfortable room even so.
 */
@Composable
private fun StatusRow(product: ProductCardData, p: CardSizeProfile) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(p.priceSlotHeight + p.priceToDetail + p.detailSlotHeight),
        contentAlignment = Alignment.CenterStart
    ) {
        when (product.availabilityState) {
            ProductAvailabilityState.ShopClosed -> StatusPill(
                label = if (p.isCompact) "Shop closed" else "Shop is closed",
                contentDescription = "Shop is closed",
                icon = Icons.Default.Schedule,
                accent = ProductShopClosedAccentColor,
                background = ProductShopClosedBackgroundColor,
                p = p
            )

            ProductAvailabilityState.OutOfStock -> StatusPill(
                label = "Out of stock",
                contentDescription = "Out of stock",
                icon = Icons.Default.Block,
                accent = ProductOutOfStockAccentColor,
                background = ProductOutOfStockBackgroundColor,
                p = p
            )

            // Unreachable by construction — ProductCard only calls StatusRow
            // when product.isPurchasable is false. Spelled out rather than
            // folded into an `else`, matching how every other `when` on
            // availabilityState in this file stays exhaustive.
            ProductAvailabilityState.Purchasable -> Unit
        }
    }
}

/**
 * The vendor badge — a bold, solid-colour highlight naming who's selling
 * this, where the "13% OFF" line used to sit on its own before that moved up
 * into PriceRow. Reachable only when the product is purchasable —
 * ProductCard routes anything else to StatusRow instead — so no availability
 * branch is needed here, just whether there's a name to show.
 *
 * Deliberately a rounded RECTANGLE (RoundedCornerShape(4.dp), tighter than
 * the first version's 6dp — closer to a straight-edged highlighter stripe),
 * not the stadium shape (percent = 50) StatusPill uses. That distinction is
 * about more than looks: StatusPill marks a STATE ("this is currently true
 * of the product"), this badge marks an IDENTITY ("this is who it's from"),
 * and giving them different silhouettes is what lets a customer tell the two
 * apart at a glance instead of reading every badge on the card to find out
 * which kind it is.
 *
 * The Box keeps its height whether or not anything renders inside. That
 * costs the full slot on a vendor-less product (a real gap, more than the
 * old plain-text line cost) and buys a grid where every card is exactly the
 * same height regardless — still worth it, because a grid row sizes to its
 * tallest card. See detailSlotHeight on CardSizeProfile for why the slot
 * itself had to grow twice now: once for a padded chip instead of bare text,
 * again for the avatar circle this content Row holds beside the name.
 */
@Composable
private fun VendorBadge(product: ProductCardData, p: CardSizeProfile) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(p.detailSlotHeight),
        contentAlignment = Alignment.CenterStart
    ) {
        if (product.vendorName.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = ProductVendorBadgeBackgroundColor,
                border = BorderStroke(1.dp, ProductVendorBadgeBorderColor)
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = p.pillPaddingH, vertical = p.pillPaddingV
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(p.pillIconGap)
                ) {
                    VendorAvatar(product = product, p = p)
                    Text(
                        text = product.vendorName,
                        style = productVendorTextStyle(p.detailSp),
                        color = ProductVendorBadgeAccentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * The circle in front of the vendor's name — their own photo when
 * vendorImageRes is set, a coloured initial when it isn't. Same fallback
 * relationship imageRes/ProductImage already has: a real resource wins when
 * there is one, and nothing downstream needs to know or care which branch
 * actually rendered.
 *
 * Both branches are a Surface(shape = CircleShape), not an Image plus a
 * separately-clipped Box — Image has no border parameter of its own, and
 * routing both branches through the same Surface construction (the same one
 * FavouriteOverlayButton already uses for its own circular puck) is what
 * guarantees a photo and an initial-fallback present at IDENTICAL size,
 * clip, and border regardless of which one a given product happens to have.
 *
 * The white ring exists for the same reason a photo profile picture usually
 * gets one when it sits on a busy or saturated background: without it, a
 * circle whose own fill is close in tone to whatever's behind it (a photo
 * with warm tones, on the yellow badge fill) can lose its edge entirely. The
 * initial's jade fill doesn't strictly need the ring — see
 * ProductVendorBadgeBackgroundColor's doc comment for that contrast number —
 * but both branches wear it so switching between "has a photo" and "doesn't
 * yet" never changes anything about the avatar except the picture itself.
 */
@Composable
private fun VendorAvatar(product: ProductCardData, p: CardSizeProfile) {
    if (product.vendorImageRes != null) {
        Surface(
            modifier = Modifier.size(p.vendorAvatarDp),
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.White)
        ) {
            Image(
                painter = painterResource(id = product.vendorImageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        Surface(
            modifier = Modifier.size(p.vendorAvatarDp),
            shape = CircleShape,
            color = JadeFreshTertiary,
            border = BorderStroke(1.dp, Color.White)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    // take(1) is always safe here: VendorBadge only opens
                    // this composable once product.vendorName.isNotBlank()
                    // has already been checked, so there is always at least
                    // one character to take.
                    text = product.vendorName.take(1).uppercase(),
                    style = productVendorAvatarInitialTextStyle(p.vendorAvatarInitialSp),
                    color = Color.White
                )
            }
        }
    }
}

/**
 * One implementation, two states, so pill geometry cannot drift between them.
 *
 * Stadium-shaped (RoundedCornerShape(percent = 50)), not the old 6dp corner —
 * a badge this small read as a slightly-rounded rectangle rather than a chip
 * at 6dp, which was most of why it looked unfinished. The border matters more
 * than it looks: this tinted fill sits at roughly 1.15:1 luminance contrast
 * against JadeFreshBackground, nowhere near enough for the SHAPE itself to
 * read against the page — so without one, the badge had no visible edge and
 * looked like a smudge rather than an object. The border is just the accent
 * colour, softened, so it doesn't compete with the text sitting on top of it.
 */
@Composable
private fun StatusPill(
    label: String,
    contentDescription: String,
    icon: ImageVector,
    accent: Color,
    background: Color,
    p: CardSizeProfile
) {
    Surface(
        shape = RoundedCornerShape(percent = 50),
        color = background,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .semantics { this.contentDescription = contentDescription }
                .padding(horizontal = p.pillPaddingH, vertical = p.pillPaddingV),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(p.pillIconGap)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(p.statusIconDp)
            )
            Text(
                text = label,
                style = productStatusTextStyle(p.statusSp),
                color = accent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Deliberately enabled in all three states: wishlisting a product from a closed
 * shop or an out-of-stock listing is the single most useful thing a customer
 * can do at that moment, and disabling it would remove the only action left.
 */
@Composable
private fun FavouriteOverlayButton(
    isWishlisted: Boolean,
    heartTint: Color,
    onFavouriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actionLabel = if (isWishlisted) "Remove from favourites" else "Add to favourites"

    Surface(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(28.dp)
            .semantics {
                contentDescription = actionLabel
                stateDescription = if (isWishlisted) "Wishlisted" else "Not wishlisted"
            }
            .clickable(role = Role.Button, onClickLabel = actionLabel, onClick = onFavouriteClick),
        shape = CircleShape,
        color = JadeFreshSurface.copy(alpha = 0.94f),
        shadowElevation = 1.dp,
        tonalElevation = 0.dp,
        border = BorderStroke(
            1.dp, if (isWishlisted) Color(0xFFE9BBBB) else ProductWishlistBorderColor
        )) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = heartTint,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

/**
 * The blocked-state row. Same component for out-of-stock and shop-closed, with
 * a different message — but now the SAME button colour; see
 * ProductNotifyAccentColor for why that stopped varying by reason.
 *
 * The label is "NOTIFY", not "Notify me" / "Notify vendor": on a 109dp card the
 * longer label leaves the quantity almost no room. The status pill directly
 * beneath already carries both the icon and the specific reason, so the button
 * only has to name the action. The full wording survives in contentDescription
 * for TalkBack.
 */
@Composable
private fun NotifyRow(
    qty: String,
    contentDescription: String,
    message: String,
    onNotifyClick: (String) -> Unit,
    p: CardSizeProfile
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(p.actionRowGap)
    ) {
        QuantityLabel(qty = qty, p = p, modifier = Modifier.weight(1f))
        RowActionButton(
            label = "NOTIFY",
            contentDescription = contentDescription,
            p = p,
            onClick = { onNotifyClick(message) })
    }
}

/**
 * NOTIFY's button. ADD used to share this composable too, via a `filled`
 * parameter that swapped in a solid jade fill to match the stepper — that's
 * gone now. See CartAction: ADD is drawn directly there, inside the same
 * Surface the stepper's content shares, because the crossfade needed ONE
 * persistent container, not two Surfaces (this one and QuantityStepper's)
 * fading against each other. RowActionButton is back to a single job — NOTIFY's
 * white pill with an accent border — which never crossfades with anything and
 * was never part of what looked or felt wrong.
 *
 * No `accent` parameter any more, on purpose. This used to take whichever
 * colour the caller's blocked-state pill was using — red for out-of-stock,
 * amber for shop-closed — which is exactly what produced a red "Out of stock"
 * pill sitting over a green NOTIFY button below it. The border and text are
 * now always ProductNotifyAccentColor; see that constant's doc comment for
 * why decoupling this from the pill's colour was the actual fix.
 *
 * Fixed to notifySlotWidth, its own constant — it does not share a width with
 * the cart control, and doesn't need to: NOTIFY and the cart action are
 * mutually exclusive per card and never appear together, so nothing has to
 * line up between them and nothing animates between them.
 *
 * No minimumInteractiveComponentSize() here. At actionButtonHeight ×
 * notifySlotWidth (48×60dp compact, 60×88dp standard) the button's own drawn
 * size already exceeds the 48dp touch minimum on BOTH axes — the modifier
 * would be a genuine no-op, and this file doesn't carry dead modifiers.
 */
@Composable
private fun RowActionButton(
    label: String, contentDescription: String, p: CardSizeProfile, onClick: () -> Unit
) {
    val shape = RoundedCornerShape(p.actionButtonRadius)

    Surface(
        modifier = Modifier
            // fillMaxHeight(), not requiredHeight(). The row box this sits in
            // is exactly actionButtonHeight tall now (see ProductCard's action
            // area), so the control just takes all of it. Nothing overflows a
            // shorter row any more — that overflow is what used to push this
            // control's top edge up onto the photograph.
            .fillMaxHeight()
            .width(p.notifySlotWidth)
            .semantics { this.contentDescription = contentDescription }
            .clip(shape)
            .clickable(role = Role.Button, onClickLabel = contentDescription, onClick = onClick),
        shape = shape,
        // Opaque white — never a translucent tinted fill. See the note at the
        // top of the file: a translucent fill over the tinted band composites
        // to mud, and the elevation below then renders through it as a grey
        // slab rather than under it as a lift.
        color = JadeFreshSurface,
        shadowElevation = 1.5.dp,
        border = BorderStroke(p.actionButtonBorder, ProductNotifyAccentColor)) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = p.actionPaddingH),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = productActionTextStyle(p.actionSp),
                color = ProductNotifyAccentColor,
                maxLines = 1,
                // Ellipsis, not the default Clip. "NOTIFY" fits its 60dp slot
                // at the system font scale with room to spare, but sp scales
                // with the user's display-size setting and the slot does not —
                // at large scales this label will outgrow it. Truncating reads
                // as a tight fit; clipping reads as a broken glyph.
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * The purchasable state.
 *
 *   quantity == 0 →  [ 500 ml        ][   ADD   ]
 *   quantity  > 0 →  [ 5…  ][  −    2    +  ]
 *
 * The control GROWS when the product goes into the cart — addSlotWidth to the
 * wider stepperSlotWidth — and the quantity label's weight(1f) box gives up
 * exactly that much, in the same frames, because Row remeasures both together.
 * On a compact card that takes the label from 37dp to 15dp, i.e. down to a
 * leading character and an ellipsis. That is the intended trade, not a
 * casualty of it: see addSlotWidth on CardSizeProfile.
 *
 * The label never MOVES, though — it is left-aligned, so a narrower box
 * ellipsises its tail rather than sliding its start. And it cannot be
 * overlapped: Row gives the control its stated width first and hands the
 * label the remainder, so however tight the row gets, the two never share a
 * pixel.
 *
 * Only the control slot's CONTENT crossfades between "ADD" and the stepper,
 * and that slot is, structurally, exactly what Vindusha's FullWidthCartAction
 * is: ONE Surface, opened here, whose colour/shape/elevation is fixed for this
 * composable's whole lifetime. The AnimatedContent inside it only ever swaps a
 * Text for a Row — never a Surface for a different Surface.
 *
 * That last sentence is the fix for the transition that still felt laggy even
 * after the two states had been given one shared width (which has since been
 * split again — see addSlotWidth). Before this, ADD was RowActionButton — its own
 * Surface, its own clip, its own shadow — and the stepper was QuantityStepper,
 * a SECOND, separate Surface with its own shadow. AnimatedContent doesn't
 * just fade text when its two branches are whole Surfaces: for the duration
 * of the crossfade it composites TWO independent shadow/clip layers on top of
 * each other, one fading out while the other fades in. That is real per-frame
 * work on every single tap, and it is also why the button visibly flickered —
 * two shadows briefly overlapping reads as a flicker, not a fade. A single
 * Surface whose CONTENT crossfades — Vindusha's approach — never has that:
 * there is only ever one shadow, and it never doubles.
 *
 * It's the same fix for ADD's colour looking wrong: its "ADD" text now sits
 * directly on the identical solid JadeFreshPrimary Surface the stepper uses,
 * exactly like Vindusha's Add-to-cart. There is no separate white-or-jade
 * pill decision to get wrong any more — one surface, one colour, both states.
 *
 * Timings and offsets in the transitionSpec are Vindusha's ProductCard values
 * (FullWidthCartAction), verbatim — 160ms in, 120ms out, it/6 and -it/8. The
 * width tween runs 200ms alongside them, deliberately a beat longer: the fade
 * finishes first and the container settles last, which reads as the control
 * opening up rather than as two effects racing. sizeTransform stays inert —
 * both branches fill whatever width the Surface currently is, so it has
 * nothing of its own to resize.
 */
@Composable
private fun CartAction(
    product: ProductCardData, quantity: Int, onQuantityChange: (Int) -> Unit, p: CardSizeProfile
) {
    var showOutOfStockDialog by remember { mutableStateOf(false) }
    val shakeOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var shakeJob by remember { mutableStateOf<Job?>(null) }

    // The one thing about this control that changes size between states. A
    // tween rather than a spring on purpose: the content crossfade beside it
    // is a tween too, and a spring's settle would finish on a different curve
    // from the fade it is supposed to be part of. FastOutSlowInEasing leaves
    // quickly and arrives gently, which is what stops a width change reading
    // as a snap.
    val slotWidth by animateDpAsState(
        targetValue = if (quantity > 0) p.stepperSlotWidth else p.addSlotWidth,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "cart_slot_width"
    )

    fun runShake() {
        shakeJob?.cancel()
        shakeJob = scope.launch {
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(4f, tween(32))
            shakeOffset.animateTo(-4f, tween(36))
            shakeOffset.animateTo(3f, tween(32))
            shakeOffset.animateTo(-3f, tween(36))
            shakeOffset.animateTo(0f, tween(32))
        }
    }

    if (showOutOfStockDialog) {
        // PORT: swap for the production OutOfStockDialog component.
        SandboxOutOfStockDialog(
            productName = product.name,
            availableStock = product.availableStock,
            onDismiss = { showOutOfStockDialog = false })
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(p.actionRowGap)
    ) {
        QuantityLabel(qty = product.qty, p = p, modifier = Modifier.weight(1f))

        // ONE Surface for the whole control slot — see the doc comment above.
        // Fixed solid jade, fixed elevation, fixed shape; the width animates
        // and the content crossfades, but the Surface itself is opened once
        // and never replaced. That is what keeps a growing control cheap:
        // there is a single shadow being remeasured, not two being composited.
        Surface(
            modifier = Modifier
                // fillMaxHeight(), not requiredHeight() — the row box is
                // exactly actionButtonHeight tall, so this takes all of it and
                // overflows nothing. The tint behind it now runs the full
                // action area (see ProductCard), so this control sits on tint
                // for its entire height — nothing for it to overflow into.
                .fillMaxHeight()
                .width(slotWidth)
                .offset(x = shakeOffset.value.dp),
            shape = RoundedCornerShape(p.actionButtonRadius),
            color = JadeFreshPrimary,
            shadowElevation = 1.5.dp
        ) {
            // targetState is `quantity > 0`, NOT `quantity`. Keying on the
            // Int would replay the slide-and-fade on every single +/− tap,
            // when the only transition worth animating is between "not in
            // the cart" and "in the cart".
            AnimatedContent(
                targetState = quantity > 0, transitionSpec = {
                    ContentTransform(
                        targetContentEnter = fadeIn(tween(160)) + slideInHorizontally(
                            animationSpec = tween(160), initialOffsetX = { it / 6 }),
                        initialContentExit = fadeOut(tween(120)) + slideOutHorizontally(
                            animationSpec = tween(120), targetOffsetX = { -it / 8 }),
                        sizeTransform = SizeTransform(clip = false)
                    )
                }, label = "cart_action_transition"
            ) { hasQuantity ->
                if (!hasQuantity) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .semantics {
                                contentDescription = "Add ${product.name} to cart"
                            }
                            .clickable(
                                role = Role.Button,
                                onClickLabel = "Add ${product.name} to cart",
                                onClick = { onQuantityChange(1) }),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ADD",
                            style = productActionTextStyle(p.actionSp),
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                } else {
                    StepperContent(
                        product = product,
                        quantity = quantity,
                        onQuantityChange = onQuantityChange,
                        onIncrementBlocked = {
                            showOutOfStockDialog = true
                            runShake()
                        },
                        p = p
                    )
                }
            }
        }
    }
}

/**
 * The stepper's content — the −/count/+ row, with no Surface of its own.
 * This used to have one (the composable was called QuantityStepper), but
 * that Surface was a SECOND graphicsLayer crossfading against
 * RowActionButton's whenever the customer tapped ADD or removed the last
 * item — two shadows and two clips, both animating at once. CartAction now
 * opens a single persistent Surface for both states, and this just fills it:
 * the same relationship Vindusha's FullWidthCartAction has between its one
 * Surface and its two Row branches.
 *
 * Sizing comes entirely from the parent now — height and width are whatever
 * CartAction's Surface is, via fillMaxSize() — so this applies no width or
 * height of its own. That matters more than it looks: the parent's width is
 * ANIMATED, and because these three cells are weighted rather than fixed,
 * they divide whatever width exists on each frame and grow smoothly with it.
 */
@Composable
private fun StepperContent(
    product: ProductCardData,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onIncrementBlocked: () -> Unit,
    p: CardSizeProfile
) {
    val stepperInteractionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "${product.name} quantity controls. Current quantity $quantity"
            }
            .clickable(
                interactionSource = stepperInteractionSource,
                indication = null,
                enabled = true,
                onClick = { }), verticalAlignment = Alignment.CenterVertically
    ) {
        LargeStepperSide(
            modifier = Modifier.weight(1.1f),
            text = "−",
            symbolSp = p.stepperSymbolSp,
            contentDescription = "Decrease ${product.name} quantity",
            onClick = { onQuantityChange((quantity - 1).coerceAtLeast(0)) })
        StepperDivider()
        StepperCountDisplay(
            quantity = quantity,
            productName = product.name,
            countSp = p.stepperCountSp,
            modifier = Modifier.weight(1f)
        )
        StepperDivider()
        LargeStepperSide(
            modifier = Modifier.weight(1.1f),
            text = "+",
            symbolSp = p.stepperSymbolSp,
            contentDescription = if (quantity >= product.availableStock) {
                "${product.name} quantity limit reached"
            } else {
                "Increase ${product.name} quantity"
            },
            tint = if (quantity >= product.availableStock) {
                Color.White.copy(alpha = 0.55f)
            } else {
                Color.White
            },
            onClick = {
                if (quantity < product.availableStock) {
                    onQuantityChange(quantity + 1)
                } else {
                    onIncrementBlocked()
                }
            })
    }
}

@Composable
private fun StepperCountDisplay(
    quantity: Int, productName: String, countSp: Float, modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .fillMaxHeight()
            .widthIn(min = 20.dp)
            .semantics { contentDescription = "$productName quantity $quantity" }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = true,
                onClick = { }), contentAlignment = Alignment.Center
    ) {
        Text("$quantity", style = productStepperCountTextStyle(countSp), color = Color.White)
    }
}

@Composable
private fun LargeStepperSide(
    modifier: Modifier,
    text: String,
    symbolSp: Float,
    contentDescription: String,
    tint: Color = Color.White,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .fillMaxHeight()
            // NO minimumInteractiveComponentSize() here, unlike every other
            // control on this card. It enforces a 48dp LAYOUT minimum, and
            // these three children share an 82dp stepper on a compact card —
            // 27.5dp for each of − and +. Two of them demanding 48dp would
            // overflow the stepper and push the "+" off its own pill.
            //
            // The touch area is still reasonable, and this is most of what
            // widening the stepper bought: each side is 27.5 × 48dp, up from
            // 23 × 48dp. Still under the guideline on one axis, and the same
            // trade every inline grocery stepper makes — the alternative is a
            // control that does not fit the card at all.
            .semantics { this.contentDescription = contentDescription }
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                role = Role.Button,
                onClickLabel = contentDescription,
                onClick = onClick
            ), contentAlignment = Alignment.Center) {
        Text(text, style = productStepperSymbolTextStyle(symbolSp), color = tint)
    }
}

@Composable
private fun StepperDivider() {
    Box(
        Modifier
            .width(1.dp)
            .fillMaxHeight(0.54f)
            .background(Color.White.copy(alpha = 0.26f))
    )
}

/** PORT: stand-in for the production OutOfStockDialog. */
@Composable
private fun SandboxOutOfStockDialog(
    productName: String, availableStock: Int, onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("Got it") } },
        title = { Text("Only $availableStock left") },
        text = { Text("We have $availableStock of \"$productName\" in stock right now.") },
        containerColor = JadeFreshSurface
    )
}

// ═════════════════════════════════════════════════════════════════════════════
//  ProductCardTestScreen — the bench.
//
//  Renders the card at the REAL production grid sizes, so nothing here is a
//  mock-up: a 360dp phone gives 109dp cards in 3 columns and 168dp in 2, which
//  is exactly what Vindusha's Gridspacing.kt computes.
//
//  The sample set is chosen to break things on purpose. Fortune Oil carries a
//  four-digit price AND a long quantity — the pair that overflowed the old
//  merged price row. Tata Salt has no discount, so you can see the reserved
//  discount slot keeping the grid even. Two products are blocked, one per
//  state, so both pills and both notify palettes are on screen at once.
// ═════════════════════════════════════════════════════════════════════════════

// imageRes below is one.png .. nine.png from res/drawable, assigned in plain
// file order — NOT matched to what's actually in each photo, since a filename
// list is all this had to go on. Swap them around freely once you can see
// which photo is which; ten.png is spare for whenever a 10th sample lands.
// tint is gone from every entry here: every sample now has a real imageRes,
// and ProductImage returns before it ever reads tint once imageRes is set —
// carrying a value that's provably never read would just be noise. It's
// still a live field on ProductCardData, for whichever product doesn't have
// a photo yet.
// vendorName is set on every purchasable sample now — including p5, which has
// no discount — because the vendor badge is driven by vendorName alone, not
// by discountPercent the way the old "13% OFF" line was; a product needs no
// discount at all to still show who it's from. p7's "Local Farmers Market"
// is deliberately the longest name in the set, on a compact card, to prove
// the badge ellipsises rather than overflowing when a name doesn't fit.
private val SampleProducts = listOf(
    ProductCardData(
        id = "p1",
        name = "Tomato Local",
        qty = "500 g",
        price = 24.0,
        mrp = 32.0,
        availableStock = 3,
        imageRes = R.drawable.one,
        vendorName = "Fresh Farm Co."
    ), ProductCardData(
        id = "p2",
        name = "Fortune Sunflower Oil",
        qty = "1 L (approx)",
        price = 1299.0,
        mrp = 1650.0,
        availableStock = 8,
        imageRes = R.drawable.two,
        vendorName = "Metro Grocers"
    ), ProductCardData(
        id = "p3",
        name = "Amul Gold Milk",
        qty = "500 ml",
        price = 34.0,
        mrp = 36.0,
        availableStock = 12,
        imageRes = R.drawable.three,
        vendorName = "City Dairy Mart"
    ), ProductCardData(
        id = "p4",
        name = "Aashirvaad Chakki Atta",
        qty = "5 kg",
        price = 245.0,
        mrp = 280.0,
        availableStock = 6,
        imageRes = R.drawable.four,
        vendorName = "Everyday Essentials"
    ),
    // No discount — the price row's "% OFF" simply doesn't render — but the
    // vendor badge below it still does, unconditionally on vendorName alone.
    ProductCardData(
        id = "p5",
        name = "Tata Salt",
        qty = "1 kg",
        price = 28.0,
        mrp = 28.0,
        availableStock = 20,
        imageRes = R.drawable.five,
        vendorName = "Daily Needs Store"
    ),
    // Out of stock.
    ProductCardData(
        id = "p6",
        name = "Britannia Brown Bread",
        qty = "400 g",
        price = 45.0,
        mrp = 50.0,
        availableStock = 0,
        imageRes = R.drawable.six
    ), ProductCardData(
        id = "p7",
        name = "Onion",
        qty = "1 kg",
        price = 32.0,
        mrp = 44.0,
        availableStock = 15,
        imageRes = R.drawable.seven,
        vendorName = "Local Farmers Market"
    ),
    // Shop closed — in stock, but still unbuyable.
    ProductCardData(
        id = "p8",
        name = "Fresh Paneer",
        qty = "200 g",
        price = 89.0,
        mrp = 99.0,
        availableStock = 9,
        imageRes = R.drawable.eight,
        vendorName = "Sharma Dairy",
        gatedState = ProductAvailabilityState.ShopClosed
    ), ProductCardData(
        id = "p9",
        name = "Maggi Noodles",
        qty = "280 g",
        price = 56.0,
        mrp = 60.0,
        availableStock = 25,
        imageRes = R.drawable.nine,
        vendorName = "QuickMart Express"
    )
)

@Composable
fun ProductCardTestScreen(
    onNotify: (String) -> Unit = {}
) {
    // Real interactive state, so the stepper, the heart and the stock cap all
    // behave the way they will in production.
    val cart = remember { mutableStateMapOf<String, Int>() }
    val wishlist = remember { mutableStateMapOf<String, Boolean>() }
    var columns by remember { mutableStateOf(3) }

    // Same arithmetic as Vindusha's Gridspacing.kt, so cardWidth here is the
    // cardWidth there. This is what makes the bench trustworthy.
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val gridPaddingH = 8.dp
    val cardSpacing = 8.dp
    val cardWidth = remember(screenWidth, columns) {
        val usable = screenWidth - (gridPaddingH.value * 2) - (cardSpacing.value * (columns - 1))
        (usable / columns).dp
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JadeFreshBackground)
    ) {
        BenchHeader(
            columns = columns, cardWidth = cardWidth, onColumnsChange = { columns = it })

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = gridPaddingH, end = gridPaddingH, top = 12.dp, bottom = 32.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(cardSpacing),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(items = SampleProducts, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    modifier = Modifier.fillMaxWidth(),
                    cardWidth = cardWidth,
                    isWishlisted = wishlist[product.id] ?: false,
                    cartQuantity = cart[product.id] ?: 0,
                    onCardClick = { },
                    onFavouriteClick = {
                        wishlist[product.id] = !(wishlist[product.id] ?: false)
                    },
                    onAddToCartClick = { newQty ->
                        if (newQty <= 0) cart.remove(product.id) else cart[product.id] = newQty
                    },
                    onNotifyClick = onNotify
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Open Caption · ${SampleProducts.size} products · " + "card ${cardWidth.value.toInt()}dp wide",
                    style = MaterialTheme.typography.labelSmall,
                    color = JadeFreshOnSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun BenchHeader(columns: Int, cardWidth: Dp, onColumnsChange: (Int) -> Unit) {
    Surface(color = JadeFreshSurface, shadowElevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Product Card Bench",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = JadeFreshPrimary
                )
                Text(
                    // isCompact flips at 140dp — worth seeing which profile you
                    // are looking at, because the two are tuned separately.
                    text = "${cardWidth.value.toInt()}dp card · " + if (cardWidth < 140.dp) "compact profile" else "standard profile",
                    style = MaterialTheme.typography.labelSmall,
                    color = JadeFreshOnSurfaceVariant
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(2, 3).forEach { n ->
                    val selected = columns == n
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onColumnsChange(n) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (selected) JadeFreshPrimary else JadeFreshSurfaceVariant,
                        border = BorderStroke(1.dp, JadeFreshBorder)
                    ) {
                        Text(
                            text = "$n col",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) Color.White else JadeFreshOnSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }
    }
}
