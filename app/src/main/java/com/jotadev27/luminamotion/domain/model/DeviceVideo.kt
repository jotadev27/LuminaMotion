package com.jotadev27.luminamotion.domain.model

/**
 * A video stored on the device (from MediaStore) that the user can preview and
 * set as a live wallpaper. The same [uri] is used both as the playback source
 * and as the image model for its thumbnail (a decoded frame).
 */
data class DeviceVideo(
    val id: Long,
    val title: String,
    val uri: String,
    val durationSeconds: Int,
) {
    /** "0:30" style label for the duration badge. */
    val durationLabel: String
        get() = "%d:%02d".format(durationSeconds / 60, durationSeconds % 60)
}
