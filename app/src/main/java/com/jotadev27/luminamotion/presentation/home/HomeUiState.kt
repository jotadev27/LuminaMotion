package com.jotadev27.luminamotion.presentation.home

import androidx.compose.runtime.Immutable
import com.jotadev27.luminamotion.domain.model.DeviceVideo

/** Immutable UI state for the Home (device-videos) screen. */
@Immutable
data class HomeUiState(
    val isLoading: Boolean = false,
    val videos: List<DeviceVideo> = emptyList(),
)
