package com.jotadev27.luminamotion.domain.usecase

import com.jotadev27.luminamotion.domain.model.WallpaperSelection
import com.jotadev27.luminamotion.domain.repository.SelectedWallpaperRepository

/** Persists the full wallpaper choice before launching the OS chooser. */
class SetWallpaperSelectionUseCase(
    private val repository: SelectedWallpaperRepository,
) {
    suspend operator fun invoke(selection: WallpaperSelection) =
        repository.setSelection(selection)
}
