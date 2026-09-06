package com.jotadev27.luminamotion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jotadev27.luminamotion.core.theme.LuminaMotionTheme
import com.jotadev27.luminamotion.presentation.navigation.LuminaNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Bridges the cold-start window with the system splash so the app never
        // shows a black/blank screen while it loads. Must run before super().
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LuminaMotionTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LuminaNavGraph()
                }
            }
        }
    }
}
