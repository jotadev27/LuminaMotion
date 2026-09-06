package com.jotadev27.luminamotion.core.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing & shape tokens from the Lumina design system.
 * Centralising these keeps layouts consistent and avoids "magic numbers".
 */
object Dimens {
    val Base = 8.dp
    val Gutter = 12.dp
    val ContainerMargin = 20.dp
    val SafeAreaBottom = 32.dp

    // Component sizing
    val IconButton = 48.dp
    val CardCorner = 12.dp        // rounded-xl in the mockup
    val ChipCorner = 9999.dp      // full

    // Responsive grid
    val GridMinCellWidth = 160.dp
}
