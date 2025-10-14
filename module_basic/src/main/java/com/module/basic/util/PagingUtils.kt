package com.module.basic.util

import com.helper.develop.paging.LoadParams
import com.helper.develop.paging.LoadResult
import com.helper.develop.paging.PagingConfig
import com.helper.develop.paging.PagingStart
import com.helper.develop.paging.buildPaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

fun <T> buildOffsetPaging(
    coroutineScope: CoroutineScope ,
    context: CoroutineContext = Dispatchers.IO,
    pagingStart: PagingStart = PagingStart.DEFAULT,
    initialKey: Int = 1,
    config: PagingConfig = PagingConfig(),
    load: suspend (LoadParams<Int>) -> List<T>?
) = buildPaging(
    coroutineScope = coroutineScope,
    context = context,
    pagingStart = pagingStart,
    initialKey = initialKey,
    config = config,
) {
    val list = load(it) ?: emptyList()
    val nextKey = if (list.isEmpty()||list.size<10) null else it.key!! + 1
    LoadResult(nextKey, list)
}


