package com.module.basic.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.module.basic.util.onClick
import kotlin.math.roundToInt

@Composable
fun AppSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
) {
    Box(
        modifier = modifier
            .height(24.dp)
            .width(43.dp)
            .then(
                if (checked) Modifier.background(
                    color = Color(0xffFF4070),
                    shape = RoundedCornerShape(20.dp)
                ) else Modifier.background(
                    color = Color(0xffFF4070).copy(0.2f),
                    shape = RoundedCornerShape(20.dp)
                )
            )
            .padding(2.dp)
    ) {
        val density = LocalDensity.current
        val animation by animateFloatAsState(targetValue = if (checked) with(density) { 40.dp.toPx() - 22.dp.toPx() } else 0f)
        Box(
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .offset {
                    IntOffset(x = animation.roundToInt(), y = 0)
                }
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(
                    color =if(checked)  Color.White else Color(0xfff5f5f5),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .onClick {
                    onCheckedChange?.invoke(!checked)
                }
        )
    }
}