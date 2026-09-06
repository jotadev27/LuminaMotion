package com.jotadev27.luminamotion.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Brand colors that don't map cleanly onto Material3 slots
 * (e.g. the "fixed" accents and the Live indicator). Exposed through a
 * CompositionLocal so any composable can read them via [LuminaTheme.colors].
 */
@Immutable
data class LuminaColors(
    val primaryFixed: Color,
    val onPrimaryFixed: Color,
    val live: Color,
    val outlineVariant: Color,
)

private val LocalLuminaColors = staticCompositionLocalOf {
    LuminaColors(
        primaryFixed = LuminaPrimaryFixed,
        onPrimaryFixed = LuminaOnPrimaryFixed,
        live = LuminaSecondaryContainer,
        outlineVariant = LuminaOutlineVariant,
    )
}

private val LuminaDarkColorScheme = darkColorScheme(
    primary = LuminaPrimary,
    onPrimary = LuminaOnPrimary,
    secondary = LuminaSecondary,
    secondaryContainer = LuminaSecondaryContainer,
    onSecondaryContainer = LuminaOnSecondaryContainer,
    background = LuminaBackground,
    onBackground = LuminaOnSurface,
    surface = LuminaBackground,
    onSurface = LuminaOnSurface,
    surfaceVariant = LuminaSurfaceVariant,
    onSurfaceVariant = LuminaOnSurfaceVariant,
    surfaceContainerLowest = LuminaSurfaceContainerLowest,
    surfaceContainerLow = LuminaSurfaceContainerLow,
    surfaceContainer = LuminaSurfaceContainer,
    surfaceContainerHigh = LuminaSurfaceContainerHigh,
    surfaceContainerHighest = LuminaSurfaceContainerHighest,
    outline = LuminaOutline,
    outlineVariant = LuminaOutlineVariant,
    error = LuminaError,
    onError = LuminaOnError,
)

/**
 * Lumina Motion is a dark-only, brand-themed surface. We intentionally ignore
 * the system light theme and dynamic color to preserve the identity.
 */
@Composable
fun LuminaMotionTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LuminaDarkColorScheme,
        typography = LuminaTypography,
        content = content,
    )
}

/** Convenience accessor for brand tokens, mirroring [MaterialTheme]. */
object LuminaTheme {
    val colors: LuminaColors
        @Composable
        @ReadOnlyComposable
        get() = LocalLuminaColors.current
}
