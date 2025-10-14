package com.helper.develop.wheel


import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import kotlin.math.absoluteValue

@Composable
fun RowScope.Wheel(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    itemCount: Int,
    itemHeight: Dp = WheelDefaults.itemHeight,
    visibleCount: Int = if(itemCount<WheelDefaults.MAX_VISIBLE_COUNT )   WheelDefaults.MIN_VISIBLE_COUNT else WheelDefaults.MAX_VISIBLE_COUNT,
    flingBehavior: FlingBehavior = rememberSnapFlingBehavior(state),
    draw: ContentDrawScope.(Rect) -> Unit = WheelDefaults.draw,
    animator: GraphicsLayerScope.(Float) -> Unit = WheelDefaults.animator,
    content: @Composable (Int) -> Unit
) {
    require(visibleCount % 2 != 0)
    LazyColumn(
        userScrollEnabled = itemCount > 0,
        contentPadding = PaddingValues(vertical = (itemHeight * (visibleCount - 1)) / 2),
        modifier = modifier
            .height(itemHeight * visibleCount)
            .weight(1f)
            .drawWithContent {
                draw( Rect(
                    left = 0f,
                    top = size.height / 2f - itemHeight.toPx() / 2f,
                    right = size.width,
                    bottom = size.height / 2f + itemHeight.toPx() / 2f,
                ))
                drawContent()
            },
        state = state,
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = flingBehavior
    ) {
        items(itemCount, key = {
            it
        }) { index ->
            Box(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(itemHeight)
                    .graphicsLayer {
                        state.layoutInfo.visibleItemsInfo
                            .find { it.index == index }
                            ?.let {
                                val offset = it.offset.absoluteValue
                                val total = ((visibleCount-1) / 2) * size.height
                                animator(offset / total)
                            }
                    },
                contentAlignment = Alignment.Center
            ) {
                content(index)
            }
        }
    }
}
