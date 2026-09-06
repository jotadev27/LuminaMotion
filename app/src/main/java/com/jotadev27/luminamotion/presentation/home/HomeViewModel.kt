package com.jotadev27.luminamotion.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jotadev27.luminamotion.domain.usecase.GetDeviceVideosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Loads the device's videos once media permission is granted. The composable
 * owns the permission request and calls [loadVideos] when access is available.
 */
class HomeViewModel(
    private val getDeviceVideos: GetDeviceVideosUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadVideos() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val videos = getDeviceVideos()
            _uiState.update { HomeUiState(isLoading = false, videos = videos) }
        }
    }
}
