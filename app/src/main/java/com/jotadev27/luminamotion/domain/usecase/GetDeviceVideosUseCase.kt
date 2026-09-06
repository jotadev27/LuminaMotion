package com.jotadev27.luminamotion.domain.usecase

import com.jotadev27.luminamotion.domain.model.DeviceVideo
import com.jotadev27.luminamotion.domain.repository.VideoRepository

/** Returns the videos stored on the device. */
class GetDeviceVideosUseCase(
    private val repository: VideoRepository,
) {
    suspend operator fun invoke(): List<DeviceVideo> = repository.getDeviceVideos()
}
