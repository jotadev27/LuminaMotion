package com.jotadev27.luminamotion.presentation.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.AspectRatio
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jotadev27.luminamotion.core.theme.Dimens
import com.jotadev27.luminamotion.core.ui.GlassIconButton
import com.jotadev27.luminamotion.core.ui.VideoPlayer
import com.jotadev27.luminamotion.core.wallpaper.LiveWallpaperLauncher
import com.jotadev27.luminamotion.presentation.LuminaViewModelFactory

/** Stateful entry point wired to [PreviewViewModel] via the DI factory. */
@Composable
fun WallpaperPreviewRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PreviewViewModel = viewModel(factory = LuminaViewModelFactory.Preview),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                PreviewEvent.LaunchWallpaperChooser -> LiveWallpaperLauncher.launch(context)
            }
        }
    }

    WallpaperPreviewScreen(
        uiState = uiState,
        onBack = onBack,
        onPlayToggled = viewModel::onPlayToggled,
        onDoubleTapVideo = viewModel::onDoubleTapVideo,
        onAudioToggled = viewModel::onAudioToggled,
        onScaleToFitToggled = viewModel::onScaleToFitToggled,
        onDoubleTapOptionToggled = viewModel::onDoubleTapOptionToggled,
        onSetWallpaper = viewModel::onSetWallpaper,
        modifier = modifier,
    )
}

@Composable
fun WallpaperPreviewScreen(
    uiState: PreviewUiState,
    onBack: () -> Unit,
    onPlayToggled: () -> Unit,
    onDoubleTapVideo: () -> Unit,
    onAudioToggled: () -> Unit,
    onScaleToFitToggled: () -> Unit,
    onDoubleTapOptionToggled: () -> Unit,
    onSetWallpaper: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (uiState.videoUri.isNotEmpty()) {
            VideoPlayer(
                videoUrl = uiState.videoUri,
                playWhenReady = uiState.isPlaying,
                muted = !uiState.isAudioOn,
                // The "scale to fit" option is not reflected in this in-app
                // preview; the video always plays full-screen here. The flag is
                // only stored and takes effect on the real wallpaper, which the
                // user sees in the system's own preview.
                scaleToFit = false,
                modifier = Modifier.fillMaxSize(),
            )
        }

        // Scrim over the video for control legibility.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                        0.45f to Color.Transparent,
                        1f to MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = Dimens.Gutter)
                .padding(top = Dimens.ContainerMargin, bottom = Dimens.SafeAreaBottom),
        ) {
            // Top: back + title.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                GlassIconButton(
                    icon = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Go back",
                    onClick = onBack,
                )
                Text(
                    text = uiState.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // Center video area. There is no on-screen play/pause indicator: a
            // single tap toggles playback and a double tap does the same when
            // that option is enabled. The gestures work whether or not an icon
            // is shown.
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onPlayToggled() },
                            onDoubleTap = { onDoubleTapVideo() },
                        )
                    },
            )

            BottomControls(
                uiState = uiState,
                onAudioToggled = onAudioToggled,
                onScaleToFitToggled = onScaleToFitToggled,
                onDoubleTapOptionToggled = onDoubleTapOptionToggled,
                onSetWallpaper = onSetWallpaper,
            )
        }
    }
}

@Composable
private fun BottomControls(
    uiState: PreviewUiState,
    onAudioToggled: () -> Unit,
    onScaleToFitToggled: () -> Unit,
    onDoubleTapOptionToggled: () -> Unit,
    onSetWallpaper: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 480.dp),
        verticalArrangement = Arrangement.spacedBy(Dimens.ContainerMargin),
    ) {
        // Options the user configures before applying.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.CardCorner))
                .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.85f))
                .padding(horizontal = Dimens.ContainerMargin, vertical = Dimens.Base),
            verticalArrangement = Arrangement.spacedBy(Dimens.Base),
        ) {
            OptionRow(
                icon = Icons.AutoMirrored.Rounded.VolumeUp,
                label = "Activar audio",
                checked = uiState.isAudioOn,
                onCheckedChange = { onAudioToggled() },
            )
            OptionRow(
                icon = Icons.Rounded.AspectRatio,
                label = "Escalar para ajustar",
                checked = uiState.scaleToFit,
                onCheckedChange = { onScaleToFitToggled() },
            )
            OptionRow(
                icon = Icons.Rounded.TouchApp,
                label = "Tocar dos veces para pausar/reanudar",
                checked = uiState.doubleTapToToggle,
                onCheckedChange = { onDoubleTapOptionToggled() },
            )
        }

        Button(
            onClick = onSetWallpaper,
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp),
        ) {
            Icon(Icons.Rounded.Wallpaper, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(
                text = "SET AS WALLPAPER",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun OptionRow(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
            ),
        )
    }
}
