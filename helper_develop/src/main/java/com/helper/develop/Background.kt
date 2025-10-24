package com.helper.develop

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import kotlin.math.max

@Composable
fun Background(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentScale: ContentScale = ContentScale.FillBounds,
    measurePainter: Boolean = false,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = {
            Image(
                painter = painter,
                contentScale = contentScale,
                contentDescription = null
            )
            content()
        },
    ) { measurables, constraints ->
        require(measurables.size == 2) {
            throw IllegalArgumentException("You must have exactly one main content")
        }
        val contentPlaceable = measurables[1].measure(constraints)
        val imagePlaceable = if (measurePainter) {
            measurables[0].measure(constraints)
        } else {
            measurables[0].measure(
                constraints.copy(
                    minWidth = contentPlaceable.width,
                    minHeight = contentPlaceable.height,
                    maxWidth = contentPlaceable.width,
                    maxHeight = contentPlaceable.height,
                )
            )
        }
        val width = max(contentPlaceable.width, imagePlaceable.width)
        val height = max(contentPlaceable.height, imagePlaceable.height)
        Log.e("1234","w=${width} h=${height}")
        layout(width, height) {
            imagePlaceable.placeRelative(0, 0)
            contentPlaceable.placeRelative(0, 0)
        }
    }
}