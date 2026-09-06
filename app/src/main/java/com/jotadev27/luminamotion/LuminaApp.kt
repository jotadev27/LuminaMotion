package com.jotadev27.luminamotion

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import com.jotadev27.luminamotion.di.AppContainer
import com.jotadev27.luminamotion.di.DefaultAppContainer

/**
 * Application entry point. Owns the [AppContainer] (DI root) and provides a Coil
 * [ImageLoader] with the video-frame decoder, so device videos can render a
 * thumbnail frame directly from their content URI.
 */
class LuminaApp : Application(), ImageLoaderFactory {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }

    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .components { add(VideoFrameDecoder.Factory()) }
            .build()
}
