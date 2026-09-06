package com.jotadev27.luminamotion.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.jotadev27.luminamotion.domain.model.WallpaperSelection
import com.jotadev27.luminamotion.domain.repository.SelectedWallpaperRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * [SelectedWallpaperRepository] backed by [SharedPreferences]. Lightweight and
 * synchronous-friendly, which suits the wallpaper service (it reads the value
 * from a non-Compose context on startup).
 */
class SelectedWallpaperRepositoryImpl(
    context: Context,
) : SelectedWallpaperRepository {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getSelection(): WallpaperSelection? {
        val uri = prefs.getString(KEY_VIDEO_URI, null) ?: return null
        return WallpaperSelection(
            videoUri = uri,
            scaleToFit = prefs.getBoolean(KEY_SCALE_TO_FIT, false),
            audioEnabled = prefs.getBoolean(KEY_AUDIO_ENABLED, false),
            doubleTapToToggle = prefs.getBoolean(KEY_DOUBLE_TAP, true),
        )
    }

    override fun observeSelection(): Flow<WallpaperSelection?> = callbackFlow {
        trySend(getSelection())
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            trySend(getSelection())
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override suspend fun setSelection(selection: WallpaperSelection) {
        prefs.edit {
            putString(KEY_VIDEO_URI, selection.videoUri)
            putBoolean(KEY_SCALE_TO_FIT, selection.scaleToFit)
            putBoolean(KEY_AUDIO_ENABLED, selection.audioEnabled)
            putBoolean(KEY_DOUBLE_TAP, selection.doubleTapToToggle)
        }
    }

    private companion object {
        const val PREFS_NAME = "lumina_wallpaper"
        const val KEY_VIDEO_URI = "selected_video_uri"
        const val KEY_SCALE_TO_FIT = "selected_scale_to_fit"
        const val KEY_AUDIO_ENABLED = "selected_audio_enabled"
        const val KEY_DOUBLE_TAP = "selected_double_tap"
    }
}
