package com.jotadev27.luminamotion.core.ui

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

/**
 * Plays [videoUrl] full-bleed using ExoPlayer.
 *
 * Playback flags are driven reactively from UI state:
 *  - [playWhenReady] play/pause
 *  - [muted] audio
 *  - [scaleToFit] true = show the whole frame (letterbox), false = fill & crop.
 *
 * The player is built/released in step with the screen lifecycle: when the screen
 * leaves the foreground (e.g. the system "set live wallpaper" preview opens on
 * top) the player is released so its video decoder is freed. Holding the decoder
 * there would collide with the wallpaper service's own decoder — a common cause
 * of force-closes on return — and keep draining battery/CPU in the background.
 */
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    playWhenReady: Boolean,
    muted: Boolean,
    scaleToFit: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Created eagerly for the first appearance, then released on STOP and rebuilt
    // on START by the observer below.
    var player by remember {
        mutableStateOf<ExoPlayer?>(
            ExoPlayer.Builder(context).build().apply {
                repeatMode = Player.REPEAT_MODE_ONE
            },
        )
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    if (player == null) {
                        player = ExoPlayer.Builder(context).build().apply {
                            repeatMode = Player.REPEAT_MODE_ONE
                        }
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    player?.release()
                    player = null
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player?.release()
            player = null
        }
    }

    val currentPlayer = player

    LaunchedEffect(currentPlayer, videoUrl) {
        currentPlayer?.apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }
    LaunchedEffect(currentPlayer, playWhenReady) {
        currentPlayer?.playWhenReady = playWhenReady
    }
    LaunchedEffect(currentPlayer, muted) {
        currentPlayer?.volume = if (muted) 0f else 1f
    }

    val resizeMode = if (scaleToFit) {
        AspectRatioFrameLayout.RESIZE_MODE_FIT
    } else {
        AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                useController = false
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
        },
        update = { view ->
            view.player = currentPlayer
            view.resizeMode = resizeMode
        },
    )
}
