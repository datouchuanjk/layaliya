package com.module.basic.ui.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.helper.develop.paging.LoadState
import com.helper.develop.paging.PagingData

@Composable
fun AppPagingBox(
    modifier: Modifier = Modifier,
    pagingData: PagingData<*>,
    contentAlignment: Alignment = Alignment.TopStart,
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
    Box(modifier = modifier, contentAlignment = contentAlignment) {
        if ( pagingData.refreshState is LoadState.Loading && skeletonContent != null) {
            skeletonContent()
        } else if ( pagingData.refreshState is LoadState.Error && errorContent != null) {
            errorContent()
        } else {
            content()
        }
    }
}