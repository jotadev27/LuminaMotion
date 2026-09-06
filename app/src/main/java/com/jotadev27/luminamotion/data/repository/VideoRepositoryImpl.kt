package com.jotadev27.luminamotion.data.repository

import com.jotadev27.luminamotion.data.local.DeviceVideoDataSource
import com.jotadev27.luminamotion.domain.model.DeviceVideo
import com.jotadev27.luminamotion.domain.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** [VideoRepository] backed by MediaStore via [DeviceVideoDataSource]. */
class VideoRepositoryImpl(
    private val dataSource: DeviceVideoDataSource,
) : VideoRepository {

    override suspend fun getDeviceVideos(): List<DeviceVideo> =
        withContext(Dispatchers.IO) { dataSource.queryVideos() }
}
