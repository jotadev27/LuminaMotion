@file:OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)

package com.jotadev27.luminamotion.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.jotadev27.luminamotion.R

/**
 * Lumina type scale, backed by the real brand fonts bundled in `res/font/`
 * (variable TTFs from Google Fonts, OFL):
 *  - Sora           → headlines / display
 *  - Inter          → body
 *  - JetBrains Mono → small labels (badges, durations)
 *
 * Each weight is pinned through the `wght` variation axis so the variable fonts
 * render the exact weight regardless of system synthesis.
 */
private fun soraFont(weight: FontWeight) =
    Font(R.font.sora, weight, variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)))

private fun interFont(weight: FontWeight) =
    Font(R.font.inter, weight, variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)))

private fun jetBrainsMonoFont(weight: FontWeight) =
    Font(R.font.jetbrains_mono, weight, variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)))

val Sora = FontFamily(
    soraFont(FontWeight.SemiBold),
    soraFont(FontWeight.Bold),
)
val Inter = FontFamily(
    interFont(FontWeight.Normal),
    interFont(FontWeight.Medium),
)
val JetBrainsMono = FontFamily(
    jetBrainsMonoFont(FontWeight.Medium),
)

val LuminaTypography = Typography(
    // display-lg — 40/48, -0.02em, 700
    displayLarge = TextStyle(
        fontFamily = Sora,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.02).em,
    ),
    // headline-lg — 32/40, 600
    headlineLarge = TextStyle(
        fontFamily = Sora,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    // headline-lg-mobile — 24/32, 600
    headlineMedium = TextStyle(
        fontFamily = Sora,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    // headline-md — 20/28, 600  (section titles, primary button label)
    headlineSmall = TextStyle(
        fontFamily = Sora,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    // body-lg — 16/24, 400
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    // body-md — 14/20, 400
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    // body-md / medium — category chips
    titleSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    // label-sm — 12/16, 0.05em, 500  (Live / quality badges, durations)
    labelSmall = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.05.em,
    ),
)
