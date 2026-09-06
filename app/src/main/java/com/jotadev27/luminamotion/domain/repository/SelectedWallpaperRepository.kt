package com.jotadev27.luminamotion.domain.repository

import com.jotadev27.luminamotion.domain.model.WallpaperSelection
import kotlinx.coroutines.flow.Flow

/**
 * Persists the user's current live-wallpaper choice (video + scaling + audio).
 * It's the single source of truth shared between the app and the wallpaper
 * service, which observes it to reload when the user picks a different video.
 */
interface SelectedWallpaperRepository {
    fun getSelection(): WallpaperSelection?

    /** Emits the current selection and again whenever it changes. */
    fun observeSelection(): Flow<WallpaperSelection?>

    suspend fun setSelection(selection: WallpaperSelection)
}
