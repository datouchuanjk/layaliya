package com.module.basic.ui.paging

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import com.helper.develop.paging.LoadState
import com.helper.develop.paging.PagingData

fun <T> LazyGridScope.items(
    pagingData: PagingData<T>,
    key: ((T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    loadingContent: (@Composable LazyGridItemScope.() -> Unit)? = { DefaultLoadingCompose() },
    notLoadingContent: (@Composable LazyGridItemScope.() -> Unit)? = {
        DefaultNotLoadingCompose(
            pagingData.appendState
        )
    },
    loadErrorContent: (@Composable LazyGridItemScope.() -> Unit)? = { DefaultLoadErrorCompose { pagingData.retry() } },
    emptyContent: (@Composable LazyGridItemScope.() -> Unit)? = { DefaultEmptyCompose() },
    itemContent: @Composable LazyGridItemScope.(item: T) -> Unit
) {
    withItems(
        pagingData = pagingData,
        loadingContent = loadingContent,
        notLoadingContent = notLoadingContent,
        loadErrorContent = loadErrorContent,
        emptyContent = emptyContent,
    ) {
        items(
            count = pagingData.count,
            key = if (key == null) {
                null
            } else {
                {
                    key(pagingData.peek(it))
                }
            },
            contentType = {
                contentType(pagingData.peek(it))
            }
        ) {
            itemContent(pagingData[it])
        }
    }
}

fun <T> LazyGridScope.itemsIndexed(
    pagingData: PagingData<T>,
    key: ((T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    loadingContent: (@Composable LazyGridItemScope.() -> Unit)? = { DefaultLoadingCompose() },
    notLoadingContent: (@Composable LazyGridItemScope.() -> Unit)? = {
        DefaultNotLoadingCompose(
            pagingData.appendState
        )
    },
    loadErrorContent: (@Composable LazyGridItemScope.() -> Unit)? = { DefaultLoadErrorCompose { pagingData.retry() } },
    emptyContent: (@Composable LazyGridItemScope.() -> Unit)? = { DefaultEmptyCompose() },
    itemContent: @Composable LazyGridItemScope.(count: Int, item: T) -> Unit
) {
    withItems(
        pagingData = pagingData,
        loadingContent = loadingContent,
        notLoadingContent = notLoadingContent,
        loadErrorContent = loadErrorContent,
        emptyContent = emptyContent,
    ) {
        items(
            count = pagingData.count,
            key = if (key == null) {
                null
            } else {
                {
                    key(pagingData.peek(it))
                }
            },
            contentType = {
                contentType(pagingData.peek(it))
            }
        ) {
            itemContent(it, pagingData[it])
        }
    }
}


private fun <T> LazyGridScope.withItems(
    pagingData: PagingData<T>,
    loadingContent: (@Composable LazyGridItemScope.() -> Unit)? = { DefaultLoadingCompose() },
    notLoadingContent: (@Composable LazyGridItemScope.() -> Unit)? = {
        DefaultNotLoadingCompose(
            pagingData.appendState
        )
    },
    loadErrorContent: (@Composable LazyGridItemScope.() -> Unit)? = { DefaultLoadErrorCompose { pagingData.retry() } },
    emptyContent: (@Composable LazyGridItemScope.() -> Unit)? = { DefaultEmptyCompose() },
    items: LazyGridScope.() -> Unit
) {
    if (pagingData.count == 0 && emptyContent != null) {
        item(
            span = {
                GridItemSpan(maxLineSpan)
            },
            key = "Empty"
        ) {
            emptyContent()
        }
    } else {
        items()
        val loadState = pagingData.appendState
        when {
            loadState is LoadState.Loading && loadingContent != null -> {
                item(
                    key = "loadingContent",
                    span = {
                        GridItemSpan(maxLineSpan)
                    },
                ) {
                    loadingContent()
                }
            }

            loadState is LoadState.Error && loadErrorContent != null -> {
                item(
                    key = "loadErrorContent",
                    span = {
                        GridItemSpan(maxLineSpan)
                    },
                ) {
                    loadErrorContent()
                }
            }

            loadState is LoadState.NotLoading && notLoadingContent != null -> {
                item(
                    key = "notLoadingContent",
                    span = {
                        GridItemSpan(maxLineSpan)
                    },
                ) {
                    notLoadingContent()
                }
            }
        }
    }
}

