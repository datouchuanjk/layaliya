package com.module.basic.ui.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.helper.develop.paging.LoadState
import com.helper.develop.paging.PagingData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPagingRefresh(
    modifier: Modifier = Modifier,
    pagingData: PagingData<*>,
    isFirstRefresh: Boolean  = pagingData.isFirstRefresh,
    isRefreshing: Boolean = pagingData.refreshState is LoadState.Loading,
    onRefresh: () -> Unit = {
        pagingData.refresh()
    },
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = {
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = state
        )
    },
    skeletonContent: @Composable (BoxScope.() -> Unit)? = {
        DefaultSkeletonCompose()
    },
    errorContent: @Composable (BoxScope.() -> Unit)? = {
        DefaultErrorCompose {
            pagingData.refresh()
        }
    },
    content: @Composable BoxScope.() -> Unit
) {
    if (isFirstRefresh && pagingData.refreshState !is LoadState.Error && skeletonContent != null) {
        Box(modifier = modifier, contentAlignment = contentAlignment) {
            skeletonContent()
        }
    } else if (isFirstRefresh && pagingData.refreshState is LoadState.Error && errorContent != null) {
        Box(modifier = modifier, contentAlignment = contentAlignment) {
            errorContent()
        }
    } else {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier,
            state = state,
            contentAlignment = contentAlignment,
            indicator = indicator,
            content = content
        )
    }
}