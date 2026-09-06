package com.jotadev27.luminamotion.core.wallpaper

import android.app.WallpaperManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings

/**
 * Launches the system "set live wallpaper" preview, pre-selecting
 * [VideoWallpaperService]. Falls back to the generic live-wallpaper picker on
 * devices that don't honor the direct intent.
 *
 * The video to render is read by the service from the repository, so callers
 * must persist the selection *before* calling [launch].
 */
object LiveWallpaperLauncher {

    fun launch(context: Context) {
        val component = ComponentName(context, VideoWallpaperService::class.java)
        val direct = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
            putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(direct)
        } catch (_: ActivityNotFoundException) {
            val fallback = Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(fallback)
            } catch (_: ActivityNotFoundException) {
                context.startActivity(
                    Intent(Settings.ACTION_DISPLAY_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                )
            }
        }
    }
}
