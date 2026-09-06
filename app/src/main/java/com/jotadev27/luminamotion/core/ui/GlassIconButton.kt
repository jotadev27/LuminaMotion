package com.jotadev27.luminamotion.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jotadev27.luminamotion.core.theme.Dimens

/**
 * Circular translucent "glass" icon button matching the `.glass-icon-btn`
 * style from the mockups (used for back / like / share controls over media).
 */
@Composable
fun GlassIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(Dimens.IconButton)
            .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.White.copy(alpha = 0.10f),
            contentColor = tint,
        ),
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}
