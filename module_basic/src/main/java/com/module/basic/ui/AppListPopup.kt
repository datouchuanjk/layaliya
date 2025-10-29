package com.module.basic.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measured
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.module.basic.util.onClick
import kotlin.math.max
import kotlin.math.min

@Composable
fun <T> AppListPopup(
    popupPosition: Density.(anchorBounds: IntRect, windowSize: IntSize, popupContentSize: IntSize) -> IntOffset,
    list: List<T>,
    map: (T) -> String,
    onSelected: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    AppPopup(
        popupPosition = popupPosition,
        onDismissRequest = onDismissRequest,
    ) {
        val maxLength = list.maxOf { map(it).length }
        val measureText = list.map { map(it) }.find { it.length == maxLength } ?: buildString {
            repeat(maxLength) {
                append("我")
            }
        }
        val rememberTextMeasurer = rememberTextMeasurer()
        val result = rememberTextMeasurer.measure(
            text = measureText,
            style = TextStyle(
                fontSize = 14.sp,
            )
        )
        val itemWidth = with(LocalDensity.current) {
            result.size.width.toDp()
        }
        val itemHeight = with(LocalDensity.current) {
            result.size.height.toDp()
        }
        Column(
            modifier = Modifier
                .background(
                    color = Color(0xfff5f5f5),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding( 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            list.forEachIndexed{ index, it ->
                Text(
                    textAlign = TextAlign.Center,
                    text = map(it),
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .height(itemHeight)
                        .width(itemWidth)
                        .onClick {
                            onDismissRequest()
                            onSelected(index)
                        })
            }
        }
    }
}