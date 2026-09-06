package com.jotadev27.luminamotion.core.theme

import androidx.compose.ui.graphics.Color

/**
 * Lumina Motion color tokens.
 *
 * Mirrors the design-system palette from the product spec (dark mode only).
 * Raw tokens live here; they are mapped to Material3 [androidx.compose.material3.ColorScheme]
 * slots and to [LuminaColors] (brand extras) in Theme.kt.
 */

// Core surfaces
val LuminaBackground = Color(0xFF131315)        // Deep charcoal
val LuminaSurfaceContainerLowest = Color(0xFF0E0E10)
val LuminaSurfaceContainerLow = Color(0xFF1C1B1D)
val LuminaSurfaceContainer = Color(0xFF201F21)
val LuminaSurfaceContainerHigh = Color(0xFF2A2A2C)
val LuminaSurfaceContainerHighest = Color(0xFF353437)
val LuminaSurfaceVariant = Color(0xFF353437)

// Foreground
val LuminaOnSurface = Color(0xFFE5E1E4)
val LuminaOnSurfaceVariant = Color(0xFFB9CACB)
val LuminaOutline = Color(0xFF849495)
val LuminaOutlineVariant = Color(0xFF3B494B)

// Accent — primary (electric cyan)
val LuminaPrimary = Color(0xFF00F0FF)           // Main CTA / accent
val LuminaPrimaryFixed = Color(0xFF7DF4FF)      // Logo + active nav/chip
val LuminaOnPrimary = Color(0xFF00363A)
val LuminaOnPrimaryFixed = Color(0xFF002022)

// Accent — secondary (vibrant magenta)
val LuminaSecondary = Color(0xFFFFADE2)
val LuminaSecondaryContainer = Color(0xFFFF2FD6)  // "Live" indicator dot
val LuminaOnSecondaryContainer = Color(0xFF530044)

val LuminaError = Color(0xFFFFB4AB)
val LuminaOnError = Color(0xFF690005)
