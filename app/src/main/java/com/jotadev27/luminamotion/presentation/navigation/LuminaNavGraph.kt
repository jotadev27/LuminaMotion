package com.jotadev27.luminamotion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jotadev27.luminamotion.presentation.home.HomeRoute
import com.jotadev27.luminamotion.presentation.preview.WallpaperPreviewRoute
import com.jotadev27.luminamotion.presentation.splash.SplashRoute

/**
 * Single source of truth for navigation. Home raises "video chosen" with the
 * content URI; the graph forwards it to the preview.
 */
@Composable
fun LuminaNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = LuminaDestination.Splash.route,
        modifier = modifier,
    ) {
        composable(LuminaDestination.Splash.route) {
            SplashRoute(
                onTimeout = {
                    navController.navigate(LuminaDestination.Home.route) {
                        // Drop the splash so Back from Home exits the app.
                        popUpTo(LuminaDestination.Splash.route) { inclusive = true }
                    }
                },
            )
        }

        composable(LuminaDestination.Home.route) {
            HomeRoute(
                onVideoClick = { video ->
                    navController.navigate(
                        LuminaDestination.Preview.createRoute(video.uri, video.title),
                    )
                },
            )
        }

        composable(
            route = LuminaDestination.Preview.route,
            arguments = listOf(
                navArgument(LuminaDestination.Preview.ARG_VIDEO_URI) {
                    type = NavType.StringType
                },
                navArgument(LuminaDestination.Preview.ARG_TITLE) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
        ) {
            WallpaperPreviewRoute(onBack = { navController.popBackStack() })
        }
    }
}
