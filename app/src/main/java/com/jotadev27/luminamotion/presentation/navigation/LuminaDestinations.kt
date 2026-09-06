package com.jotadev27.luminamotion.presentation.navigation

import android.net.Uri

/**
 * Route definitions for the nav graph. The preview receives the chosen video's
 * content URI (and title) as encoded query arguments.
 */
sealed interface LuminaDestination {
    val route: String

    data object Splash : LuminaDestination {
        override val route = "splash"
    }

    data object Home : LuminaDestination {
        override val route = "home"
    }

    data object Preview : LuminaDestination {
        const val ARG_VIDEO_URI = "videoUri"
        const val ARG_TITLE = "title"
        override val route = "preview?$ARG_VIDEO_URI={$ARG_VIDEO_URI}&$ARG_TITLE={$ARG_TITLE}"

        fun createRoute(videoUri: String, title: String): String =
            "preview?$ARG_VIDEO_URI=${Uri.encode(videoUri)}&$ARG_TITLE=${Uri.encode(title)}"
    }
}
