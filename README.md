# Lumina Motion

**Lumina Motion** is an Android app that turns the videos already stored on your
phone into animated live wallpapers. Browse your videos in a clean grid, preview
any of them full-screen, fine-tune how it looks, and set it as your home-screen
wallpaper in a couple of taps.

The app works fully offline. It makes no network requests and collects no data:
it only reads your local videos (with your permission) to display and apply them.

---

## Features

- **Device video gallery** — a responsive grid of your videos, each tile showing
  a real frame decoded straight from the file.
- **Full-screen preview** — tap to play/pause, toggle audio, switch between
  *scale to fit* and *fill & crop*, and enable double-tap to pause.
- **Live wallpaper** — set any video as an animated wallpaper. It loops, stays
  muted by default, and pauses automatically while it is not visible to save
  battery.

---

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM + Clean Architecture (the domain layer is pure Kotlin,
  with no Android dependencies)
- **Dependency injection:** manual `AppContainer` (ready to migrate to Hilt)
- **Media:** Media3 / ExoPlayer for playback and wallpaper rendering
- **Images:** Coil with a video-frame decoder for thumbnails

---

## Project structure

```
com.jotadev27.luminamotion
├── core/          Theme, reusable UI and the live-wallpaper service
├── domain/        Models, repository contracts and use cases (pure Kotlin)
├── data/          MediaStore data source and repository implementations
├── di/            AppContainer — the single composition root
└── presentation/  Navigation, Home and Preview screens (ViewModel + UI state)
```

See [ARCHITECTURE.md](ARCHITECTURE.md) for a deeper explanation of the layers and
the data flow.

---

## Requirements

- Android Studio (latest stable)
- Android SDK 36
- Minimum supported device: **Android 8.0 (API 26)**

---

## Build

```bash
# Debug build (for testing on your own device)
./gradlew :app:assembleDebug

# Release build (optimized and obfuscated with R8)
./gradlew :app:assembleRelease
```

The release build has code minification and resource shrinking enabled, so the
output is significantly smaller than the debug build.

---

## Permissions

| Permission | Why it is used |
| --- | --- |
| `READ_MEDIA_VIDEO` (Android 13+) | List the videos on the device |
| `READ_EXTERNAL_STORAGE` (Android 12 and below) | Same, on older versions |

No internet permission is requested. Nothing leaves the device.

---

## License

This project is released under a **proprietary license — all rights reserved**.
Viewing the source is allowed, but copying, modifying, redistributing or
publishing it is not permitted without written authorization. See the
[LICENSE](LICENSE) file for the full terms. Bundled fonts keep their own
SIL Open Font License.

---

Designed and developed by **[jotadev27](https://github.com/jotadev27)**.
