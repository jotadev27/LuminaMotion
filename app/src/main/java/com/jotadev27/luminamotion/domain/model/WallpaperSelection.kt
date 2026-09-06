package com.jotadev27.luminamotion.domain.model

/**
 * Everything the live wallpaper needs to render the user's choice: which video,
 * how to scale it, whether audio is on, and whether a double tap on the
 * wallpaper pauses/resumes playback. Configured in the preview before
 * "Set as Wallpaper" and read by the wallpaper service.
 */
data class WallpaperSelection(
    val videoUri: String,
    /** true = fit the whole video (letterbox); false = fill the screen (crop). */
    val scaleToFit: Boolean,
    val audioEnabled: Boolean,
    /** Double-tapping the live wallpaper toggles play/pause. */
    val doubleTapToToggle: Boolean,
)
