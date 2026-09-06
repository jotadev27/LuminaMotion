package com.jotadev27.luminamotion.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.jotadev27.luminamotion.core.theme.Dimens
import com.jotadev27.luminamotion.core.theme.LuminaTheme

/** Minimal top app bar: the LUMINA wordmark + a short subtitle. */
@Composable
fun LuminaTopBar(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(horizontal = Dimens.ContainerMargin, vertical = Dimens.Base),
    ) {
        Text(
            text = "LUMINA",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = LuminaTheme.colors.primaryFixed,
        )
        Text(
            text = "Choose a video for your wallpaper",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
