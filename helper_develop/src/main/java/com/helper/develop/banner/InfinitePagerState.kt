package com.helper.develop.banner

import androidx.annotation.FloatRange
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable

@Composable
fun rememberInfinitePagerState(
    initialPage: Int = 0,
    @FloatRange(from = -0.5, to = 0.5)
    initialPageOffsetFraction: Float = 0f,
    pageCount: () -> Int
): PagerState {
    return rememberPagerState(
        initialPage = (Int.MAX_VALUE / 2).run {
            this - this % pageCount() + initialPage
        },
        initialPageOffsetFraction = initialPageOffsetFraction,
        pageCount = {
            Int.MAX_VALUE
        }
    )
}


