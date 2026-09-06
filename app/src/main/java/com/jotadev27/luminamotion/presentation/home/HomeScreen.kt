package com.jotadev27.luminamotion.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jotadev27.luminamotion.core.theme.Dimens
import com.jotadev27.luminamotion.domain.model.DeviceVideo
import com.jotadev27.luminamotion.presentation.LuminaViewModelFactory
import com.jotadev27.luminamotion.presentation.home.components.LuminaTopBar
import com.jotadev27.luminamotion.presentation.home.components.VideoGridItem

/** Permission to read the user's videos (version-dependent). */
private val VIDEO_PERMISSION =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_VIDEO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

/**
 * Stateful entry point: owns the media-permission flow, then renders the grid of
 * device videos from [HomeViewModel].
 */
@Composable
fun HomeRoute(
    onVideoClick: (DeviceVideo) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = LuminaViewModelFactory.Home),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, VIDEO_PERMISSION) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasPermission = granted
        if (granted) viewModel.loadVideos()
    }

    LaunchedEffect(Unit) {
        if (hasPermission) viewModel.loadVideos() else permissionLauncher.launch(VIDEO_PERMISSION)
    }

    HomeScreen(
        uiState = uiState,
        hasPermission = hasPermission,
        onGrantPermission = { permissionLauncher.launch(VIDEO_PERMISSION) },
        onVideoClick = onVideoClick,
        modifier = modifier,
    )
}

/** Stateless content — driven entirely by [HomeUiState] + permission flag. */
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    hasPermission: Boolean,
    onGrantPermission: () -> Unit,
    onVideoClick: (DeviceVideo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { LuminaTopBar() },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when {
                !hasPermission -> PermissionRequest(onGrantPermission)
                uiState.isLoading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                uiState.videos.isEmpty() -> EmptyState()
                else -> VideoGrid(videos = uiState.videos, onVideoClick = onVideoClick)
            }
        }
    }
}

@Composable
private fun VideoGrid(
    videos: List<DeviceVideo>,
    onVideoClick: (DeviceVideo) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.ContainerMargin),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(videos, key = { it.id }) { video ->
            VideoGridItem(video = video, onClick = { onVideoClick(video) })
        }
    }
}

@Composable
private fun PermissionRequest(onGrantPermission: () -> Unit) {
    InfoState(
        title = "Access your videos",
        message = "Lumina needs permission to read the videos on your device so you can set one as a live wallpaper.",
        actionLabel = "Allow access",
        onAction = onGrantPermission,
    )
}

@Composable
private fun EmptyState() {
    InfoState(
        title = "No videos found",
        message = "Add some videos to your device and they'll show up here.",
    )
}

@Composable
private fun InfoState(
    title: String,
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier.padding(Dimens.ContainerMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = Icons.Rounded.VideoLibrary,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp),
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (actionLabel != null && onAction != null) {
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text(actionLabel)
            }
        }
    }
}
