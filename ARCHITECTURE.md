# Lumina Motion — Architecture

A **live wallpaper** app (Jetpack Compose) that lists the **videos stored on the
phone itself** and lets you set any of them as an animated background. Built with
**MVVM + Clean Architecture**: layers with a single responsibility and
dependencies that always point inward (UI → domain ← data).

## App flow

1. **On launch** the app requests video access permission (`READ_MEDIA_VIDEO` on
   Android 13+, `READ_EXTERNAL_STORAGE` on earlier versions).
2. **Home** shows a grid of the device's videos (each thumbnail is a frame
   decoded from the video itself).
3. Tapping one opens **Preview** full-screen (plays the video, audio toggle) →
   **SET AS WALLPAPER** opens the system picker and applies it as a live
   wallpaper.

## Layers

```
com.jotadev27.luminamotion
├── LuminaApp.kt            Application + DI root + ImageLoader (video decoder)
├── MainActivity.kt         Hosts the NavGraph inside the theme
├── di/                     AppContainer (manual DI, ready to migrate to Hilt)
├── core/
│   ├── theme/              Design system: Color, Type (Sora/Inter/Mono), Dimens, Theme
│   ├── ui/                 Reusables: GlassIconButton, VideoPlayer (ExoPlayer)
│   └── wallpaper/          VideoWallpaperService (ExoPlayer) + LiveWallpaperLauncher
├── domain/                 Pure Kotlin, NO Android dependencies
│   ├── model/              DeviceVideo
│   ├── repository/         VideoRepository, SelectedWallpaperRepository (the "seam")
│   └── usecase/            GetDeviceVideosUseCase, SetWallpaperSelectionUseCase
├── data/
│   ├── local/              DeviceVideoDataSource (MediaStore)
│   └── repository/         VideoRepositoryImpl + SelectedWallpaperRepositoryImpl
└── presentation/
    ├── navigation/         Routes + NavGraph (single source of truth for navigation)
    ├── home/               HomeScreen (permission gate) + ViewModel + UiState + components/
    └── preview/            WallpaperPreviewScreen + ViewModel + UiState
```

## Rules

- **Unidirectional data flow (UDF):** the `ViewModel` exposes an immutable
  `StateFlow<UiState>`; the Composable renders it and emits events upward.
- **Stateless screens:** each screen has a `*Route` (permissions + ViewModel) and
  a pure `*Screen` that only receives `UiState` + lambdas.
- **The domain knows nothing about Android.** Replacing MediaStore with another
  source only affects `data/` (the `VideoRepository` interface does not change).
- **Responsive:** the grid uses `GridCells.Adaptive(160.dp)` (phone → 2 columns,
  tablet/landscape → more).

## Videos and live wallpaper

- **Thumbnails:** `LuminaApp` registers a Coil `ImageLoader` with
  `VideoFrameDecoder`, so each tile paints a frame of the video from its
  `content://` URI.
- **Preview:** `VideoPlayer` mounts ExoPlayer/Media3 inside an `AndroidView`,
  center-cropped and looping; the audio toggle controls muting.
- **Set as wallpaper:** `PreviewViewModel` persists the chosen URI
  (`SelectedWallpaperRepository` backed by `SharedPreferences`) and emits a
  **one-shot event**; the screen opens the OS picker via `LiveWallpaperLauncher`
  (`ACTION_CHANGE_LIVE_WALLPAPER`).
- **VideoWallpaperService:** a `WallpaperService` whose `Engine` mounts ExoPlayer
  on the wallpaper surface, reads the selected URI from the repository, plays it
  looped and muted, and pauses when not visible (battery). Declared in the
  manifest with `BIND_WALLPAPER`.

## Design tokens — `core/theme/`

- Background `#131315`, cyan accent `#00f0ff`, fixed cyan `#7df4ff`, magenta
  `#ff2fd6`.
- Real fonts in `res/font/` (variable TTFs, OFL): **Sora**, **Inter**,
  **JetBrains Mono**, with weight pinned through the `wght` axis
  (`FontVariation`).

## How to grow it

- **DI:** migrate `AppContainer` to Hilt (the ViewModels do not change, only
  their factory).
- **Screen selection:** let the user choose between home / lock screen (the OS
  intent already allows it after applying).
- **Tests:** use cases and ViewModels are JVM-testable (no Android).
