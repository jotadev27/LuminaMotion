package com.jotadev27.luminamotion.core.wallpaper

import android.service.wallpaper.WallpaperService
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceHolder
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.jotadev27.luminamotion.LuminaApp
import com.jotadev27.luminamotion.domain.model.WallpaperSelection
import com.jotadev27.luminamotion.domain.repository.SelectedWallpaperRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Renders the user's chosen video as a live wallpaper.
 *
 * Each [Engine] owns its own [ExoPlayer] bound to the wallpaper surface and
 * observes the saved [WallpaperSelection] (so changing the video/scaling/audio
 * and re-applying updates the running wallpaper). When the user enabled it,
 * **double-tapping the wallpaper itself** pauses/resumes playback. Playback also
 * pauses when the wallpaper isn't visible, to save battery.
 */
class VideoWallpaperService : WallpaperService() {

    private val selectedWallpaperRepository: SelectedWallpaperRepository
        get() = (application as LuminaApp).container.selectedWallpaperRepository

    override fun onCreateEngine(): Engine = VideoEngine()

    private inner class VideoEngine : Engine() {

        private var player: ExoPlayer? = null
        private var scope: CoroutineScope? = null
        private var currentUri: String? = null

        private var doubleTapEnabled: Boolean = true
        /** User paused via double tap; kept separate from visibility pausing. */
        private var userPaused: Boolean = false

        private val gestureDetector = GestureDetector(
            applicationContext,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean = true
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    if (doubleTapEnabled) togglePause()
                    return true
                }
            },
        )

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            // Ask the host launcher to forward touch events to the wallpaper.
            setTouchEventsEnabled(true)
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            val exo = ExoPlayer.Builder(applicationContext).build().apply {
                repeatMode = Player.REPEAT_MODE_ONE
                setVideoSurface(holder.surface)
            }
            player = exo

            val engineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
            scope = engineScope
            selectedWallpaperRepository.observeSelection()
                .distinctUntilChanged()
                .onEach { selection -> applySelection(exo, selection) }
                .launchIn(engineScope)
        }

        private fun applySelection(exo: ExoPlayer, selection: WallpaperSelection?) {
            if (selection == null) return
            doubleTapEnabled = selection.doubleTapToToggle
            exo.volume = if (selection.audioEnabled) 1f else 0f
            exo.videoScalingMode = if (selection.scaleToFit) {
                C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            } else {
                C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            }
            if (selection.videoUri != currentUri) {
                currentUri = selection.videoUri
                userPaused = false
                exo.setMediaItem(MediaItem.fromUri(selection.videoUri))
                exo.prepare()
            }
            exo.playWhenReady = isVisible && !userPaused
        }

        private fun togglePause() {
            userPaused = !userPaused
            player?.playWhenReady = isVisible && !userPaused
        }

        override fun onTouchEvent(event: MotionEvent) {
            super.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            player?.playWhenReady = visible && !userPaused
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            scope?.cancel()
            scope = null
            player?.release()
            player = null
            currentUri = null
        }
    }
}
