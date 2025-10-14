package com.module.basic.ui

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.basic.util.onClick
import kotlin.math.roundToInt

@Composable
fun AppTabRow(
    modifier: Modifier = Modifier,
    withBackIcon: Boolean = false,
    backIconTint: Color? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    selectedTextStyle: TextStyle = TextStyle(
        color = Color(0xffFF4070),
        fontSize = 20.sp
    ),
    unselectedTextStyle: TextStyle = TextStyle(
        color = Color(0xff333333),
        fontSize = 20.sp
    ),
    space: Dp = 24.dp,
    indicatorBackground: Modifier = Modifier.background(
        color = Color(0xffFF4070),
        shape = RoundedCornerShape(2.dp)
    ),
    onBack: (OnBackPressedDispatcherOwner?) -> Unit = {
        it?.onBackPressedDispatcher?.onBackPressed()
    },
    tabs: Array<String>,
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit
) {
    val onBackPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (withBackIcon) {
            AppBackIcon(
                modifier = Modifier.padding(
                    start = contentPadding.calculateLeftPadding(
                        LayoutDirection.Ltr
                    )
                ),
                tint = backIconTint,
            ) {
                onBack(onBackPressedDispatcherOwner)
            }
        }
        Column {
            val positions = remember {
                mutableStateMapOf<Int, Rect>()
            }
            val state = rememberLazyListState()
            LaunchedEffect(selectedIndex) {
                if (state.firstVisibleItemIndex < selectedIndex) {
                    state.animateScrollToItem(selectedIndex)
                } else   if (selectedIndex<state.firstVisibleItemIndex ) {
                    state.animateScrollToItem(selectedIndex)
                }
            }
            LazyRow(
                state = state,
                horizontalArrangement = Arrangement.spacedBy(space),
                contentPadding = contentPadding
            ) {
                itemsIndexed(items = tabs) { index, text ->
                    Text(
                        text = text,
                        style = if (index == selectedIndex) selectedTextStyle else unselectedTextStyle,
                        modifier = Modifier
                            .onGloballyPositioned {
                                positions[index] = it.boundsInParent()
                            }
                            .onClick {
                                onIndexChanged(index)
                            }
                    )
                }
            }
            SpacerHeight(3.dp)
            TabIndicator(
                indicatorBackground
            ) {
                positions[selectedIndex]
            }
        }
    }
}

@Composable
private fun TabIndicator(
    modifier: Modifier,
    block: () -> Rect?
) {
    val position = block() ?: return
    val tabIndicatorWidth = position.width / 2
    val tabIndicatorWidthDp = with(LocalDensity.current) { tabIndicatorWidth.toDp() }
    val x = position.left + tabIndicatorWidth / 2
    val indicatorOffset by animateFloatAsState(
        targetValue = x,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    Box(
        modifier = Modifier
            .offset {
                IntOffset(indicatorOffset.roundToInt(), 0.dp.roundToPx())
            }
            .width(tabIndicatorWidthDp)
            .then(modifier)
            .height(2.dp)
    )
}