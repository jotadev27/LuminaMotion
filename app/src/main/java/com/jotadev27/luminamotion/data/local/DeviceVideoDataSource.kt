package com.jotadev27.luminamotion.data.local

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.jotadev27.luminamotion.domain.model.DeviceVideo

/**
 * Reads the device's videos from MediaStore. Returns each video's content URI
 * (used for both playback and thumbnail) plus title and duration.
 */
class DeviceVideoDataSource(
    private val context: Context,
) {
    fun queryVideos(): List<DeviceVideo> {
        val collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
        )
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        val videos = mutableListOf<DeviceVideo>()
        context.contentResolver.query(collection, projection, null, null, sortOrder)
            ?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(collection, id)
                    videos += DeviceVideo(
                        id = id,
                        title = cursor.getString(nameColumn) ?: "Video",
                        uri = uri.toString(),
                        durationSeconds = (cursor.getLong(durationColumn) / 1000).toInt(),
                    )
                }
            }
        return videos
    }
}
