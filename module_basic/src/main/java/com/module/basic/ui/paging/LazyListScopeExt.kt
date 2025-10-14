package com.module.basic.ui.paging

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import com.helper.develop.paging.LoadState
import com.helper.develop.paging.PagingData

fun <T> LazyListScope.items(
    pagingData: PagingData<T>,
    key: ((T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    loadingContent: (@Composable LazyItemScope.() -> Unit)? = { DefaultLoadingCompose() },
    notLoadingContent: (@Composable LazyItemScope.() -> Unit)? = {
        DefaultNotLoadingCompose(
            pagingData.appendState
        )
    },
    loadErrorContent: (@Composable LazyItemScope.() -> Unit)? = { DefaultLoadErrorCompose { pagingData.retry() } },
    emptyContent: (@Composable LazyItemScope.() -> Unit)? = { DefaultEmptyCompose() },
    itemContent: @Composable LazyItemScope.(item: T) -> Unit
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

fun <T> LazyListScope.itemsIndexed(
    pagingData: PagingData<T>,
    key: ((T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    loadingContent: (@Composable LazyItemScope.() -> Unit)? = { DefaultLoadingCompose() },
    notLoadingContent: (@Composable LazyItemScope.() -> Unit)? = {
        DefaultNotLoadingCompose(
            pagingData.appendState
        )
    },
    loadErrorContent: (@Composable LazyItemScope.() -> Unit)? = { DefaultLoadErrorCompose { pagingData.retry() } },
    emptyContent: (@Composable LazyItemScope.() -> Unit)? = { DefaultEmptyCompose() },
    itemContent: @Composable LazyItemScope.(count: Int, item: T) -> Unit
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


private fun <T> LazyListScope.withItems(
    pagingData: PagingData<T>,
    loadingContent: (@Composable LazyItemScope.() -> Unit)? = { DefaultLoadingCompose() },
    notLoadingContent: (@Composable LazyItemScope.() -> Unit)? = {
        DefaultNotLoadingCompose(
            pagingData.appendState
        )
    },
    loadErrorContent: (@Composable LazyItemScope.() -> Unit)? = { DefaultLoadErrorCompose { pagingData.retry() } },
    emptyContent: (@Composable LazyItemScope.() -> Unit)? = { DefaultEmptyCompose() },
    items: LazyListScope.() -> Unit
) {
    if (pagingData.count == 0 && emptyContent != null) {
        item(
            key = "Empty"
        ) {
            emptyContent()
        }
    } else {
        items()
        val loadState = pagingData.appendState
        when {
            loadState is LoadState.Loading && loadingContent != null -> {
                item(key = "loadingContent") {
                    loadingContent()
                }
            }

            loadState is LoadState.Error && loadErrorContent != null -> {
                item(key = "loadErrorContent") {
                    loadErrorContent()
                }
            }

            loadState is LoadState.NotLoading && notLoadingContent != null -> {
                item(key = "notLoadingContent") {
                    notLoadingContent()
                }
            }
        }
    }
}

