package com.jotadev27.luminamotion.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jotadev27.luminamotion.R
import com.jotadev27.luminamotion.core.theme.Dimens
import com.jotadev27.luminamotion.core.theme.LuminaTheme
import kotlinx.coroutines.delay

/** How long the branded presentation lingers before handing off to Home. */
private const val SPLASH_DURATION_MS = 2200L

/**
 * Stateful splash: shows the branded presentation, then raises [onTimeout] so the
 * nav graph can move on to Home. The system splash (installed in MainActivity)
 * already covered the cold-start gap; this continues that experience seamlessly
 * with the same background so there's no flash between them.
 */
@Composable
fun SplashRoute(
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS)
        onTimeout()
    }
    SplashScreen(modifier = modifier)
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val contentAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(durationMillis = 700))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Logo + welcome, centered.
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(contentAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.mipmap.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(168.dp),
            )
            Text(
                text = "LUMINA",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = LuminaTheme.colors.primaryFixed,
            )
            Spacer(Modifier.height(Dimens.Base))
            Text(
                text = "Welcome to Lumina Motion",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        // Author credit pinned to the very bottom, just above the nav bar.
        Text(
            text = "DESIGN BY jotadev27",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = Dimens.ContainerMargin)
                .alpha(contentAlpha.value),
        )
    }
}
