package com.helper.develop.banner

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay

@Composable
fun PagerState.withAutoScroll(
    interval: Long = 3000,
    animationSpec: AnimationSpec<Float> = tween(500)
) = apply {
    val isDragged by interactionSource.collectIsDraggedAsState()
    if (!isDragged) {
        LaunchedEffect(settledPage) {
            delay(interval)
            val next = currentPage
                .plus(1)
                .mod(pageCount)
            animateScrollToPage(
                page = next,
                animationSpec = animationSpec
            )
        }
    }
}