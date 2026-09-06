package com.jotadev27.luminamotion.domain.repository

import com.jotadev27.luminamotion.domain.model.DeviceVideo

/**
 * Source of the device's videos. The implementation reads MediaStore; the domain
 * only knows it can ask for the list (the dependency rule points inward).
 */
interface VideoRepository {
    /** Videos on the device, newest first. Requires media-read permission. */
    suspend fun getDeviceVideos(): List<DeviceVideo>
}
