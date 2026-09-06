package com.jotadev27.luminamotion.di

import android.content.Context
import com.jotadev27.luminamotion.data.local.DeviceVideoDataSource
import com.jotadev27.luminamotion.data.repository.SelectedWallpaperRepositoryImpl
import com.jotadev27.luminamotion.data.repository.VideoRepositoryImpl
import com.jotadev27.luminamotion.domain.repository.SelectedWallpaperRepository
import com.jotadev27.luminamotion.domain.repository.VideoRepository
import com.jotadev27.luminamotion.domain.usecase.GetDeviceVideosUseCase
import com.jotadev27.luminamotion.domain.usecase.SetWallpaperSelectionUseCase

/**
 * Manual dependency container — the single composition root of the app.
 *
 * Framework-light so the wiring is obvious and testable. Can be swapped for
 * Hilt/Koin later without touching the ViewModels.
 */
interface AppContainer {
    val getDeviceVideos: GetDeviceVideosUseCase
    val setWallpaperSelection: SetWallpaperSelectionUseCase

    /** Exposed for the [android.service.wallpaper.WallpaperService] to read. */
    val selectedWallpaperRepository: SelectedWallpaperRepository
}

class DefaultAppContainer(
    private val context: Context,
) : AppContainer {

    private val videoDataSource: DeviceVideoDataSource by lazy { DeviceVideoDataSource(context) }
    private val videoRepository: VideoRepository by lazy { VideoRepositoryImpl(videoDataSource) }

    override val selectedWallpaperRepository: SelectedWallpaperRepository by lazy {
        SelectedWallpaperRepositoryImpl(context)
    }

    override val getDeviceVideos by lazy { GetDeviceVideosUseCase(videoRepository) }
    override val setWallpaperSelection by lazy {
        SetWallpaperSelectionUseCase(selectedWallpaperRepository)
    }
}
