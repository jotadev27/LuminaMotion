package com.jotadev27.luminamotion.presentation

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jotadev27.luminamotion.LuminaApp
import com.jotadev27.luminamotion.presentation.home.HomeViewModel
import com.jotadev27.luminamotion.presentation.navigation.LuminaDestination
import com.jotadev27.luminamotion.presentation.preview.PreviewViewModel

/** Pulls the [com.jotadev27.luminamotion.di.AppContainer] off the Application. */
private val CreationExtras.container
    get() = (this[APPLICATION_KEY] as LuminaApp).container

/**
 * Wires ViewModels to their dependencies from the manual DI container. Each
 * screen calls the matching factory in `viewModel(factory = …)`.
 */
object LuminaViewModelFactory {

    val Home = viewModelFactory {
        initializer {
            HomeViewModel(getDeviceVideos = container.getDeviceVideos)
        }
    }

    val Preview = viewModelFactory {
        initializer {
            val savedStateHandle = createSavedStateHandle()
            // Navigation already URL-decodes query arguments into the handle.
            val videoUri = checkNotNull(
                savedStateHandle.get<String>(LuminaDestination.Preview.ARG_VIDEO_URI),
            ) { "PreviewViewModel requires a '${LuminaDestination.Preview.ARG_VIDEO_URI}' argument" }
            val title = savedStateHandle.get<String>(LuminaDestination.Preview.ARG_TITLE).orEmpty()
            PreviewViewModel(
                videoUri = videoUri,
                videoTitle = title,
                setWallpaperSelection = container.setWallpaperSelection,
            )
        }
    }
}
