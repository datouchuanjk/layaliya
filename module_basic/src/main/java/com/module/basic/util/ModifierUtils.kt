package com.module.basic.util

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.*

fun Modifier.onClick(
    enabled: Boolean = true,
    onClick: () -> Unit
) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        role = null,
        enabled = enabled,
        onClick = onClick
    )
}


fun Modifier.appBrushBackground(shape: Shape = RectangleShape,) = background(
     brush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xffFF76BD),
            Color(0xffFF4070),
        )
    ), shape = shape
)