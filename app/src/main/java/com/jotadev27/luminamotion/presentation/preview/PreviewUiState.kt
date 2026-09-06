package com.jotadev27.luminamotion.presentation.preview

import androidx.compose.runtime.Immutable

/**
 * Immutable UI state for the full-screen video preview. [scaleToFit] and
 * [isAudioOn] are carried into the wallpaper; [doubleTapToToggle] is a preview
 * convenience gesture.
 */
@Immutable
data class PreviewUiState(
    val videoUri: String = "",
    val title: String = "",
    val isPlaying: Boolean = true,
    val isAudioOn: Boolean = false,
    val scaleToFit: Boolean = false,
    val doubleTapToToggle: Boolean = true,
)
