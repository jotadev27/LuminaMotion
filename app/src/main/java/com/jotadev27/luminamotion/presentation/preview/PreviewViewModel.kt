package com.jotadev27.luminamotion.presentation.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jotadev27.luminamotion.domain.model.WallpaperSelection
import com.jotadev27.luminamotion.domain.usecase.SetWallpaperSelectionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** One-off side effects the screen must perform (not part of the rendered state). */
sealed interface PreviewEvent {
    data object LaunchWallpaperChooser : PreviewEvent
}

/**
 * Drives the video preview. The video URI/title come from the navigation
 * arguments; playback, audio, scaling and the double-tap option are held here
 * and (for the wallpaper-relevant ones) persisted on "Set as Wallpaper".
 */
class PreviewViewModel(
    videoUri: String,
    videoTitle: String,
    private val setWallpaperSelection: SetWallpaperSelectionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PreviewUiState(videoUri = videoUri, title = videoTitle),
    )
    val uiState: StateFlow<PreviewUiState> = _uiState.asStateFlow()

    private val _events = Channel<PreviewEvent>(Channel.BUFFERED)
    val events: Flow<PreviewEvent> = _events.receiveAsFlow()

    fun onPlayToggled() = _uiState.update { it.copy(isPlaying = !it.isPlaying) }

    fun onAudioToggled() = _uiState.update { it.copy(isAudioOn = !it.isAudioOn) }

    fun onScaleToFitToggled() = _uiState.update { it.copy(scaleToFit = !it.scaleToFit) }

    fun onDoubleTapOptionToggled() =
        _uiState.update { it.copy(doubleTapToToggle = !it.doubleTapToToggle) }

    /** Invoked by a double tap on the video; only acts when the option is on. */
    fun onDoubleTapVideo() {
        if (_uiState.value.doubleTapToToggle) onPlayToggled()
    }

    fun onSetWallpaper() {
        val state = _uiState.value
        if (state.videoUri.isEmpty()) return
        viewModelScope.launch {
            setWallpaperSelection(
                WallpaperSelection(
                    videoUri = state.videoUri,
                    scaleToFit = state.scaleToFit,
                    audioEnabled = state.isAudioOn,
                    doubleTapToToggle = state.doubleTapToToggle,
                ),
            )
            _events.send(PreviewEvent.LaunchWallpaperChooser)
        }
    }
}
