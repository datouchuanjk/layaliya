package com.helper.develop.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

fun <Key, Value> buildPaging(
    coroutineScope: CoroutineScope ,
    context: CoroutineContext = Dispatchers.IO,
    pagingStart: PagingStart = PagingStart.DEFAULT,
    initialKey: Key? = null,
    config: PagingConfig = PagingConfig(),
    load: suspend (LoadParams<Key>) -> LoadResult<Key, Value>
): Paging<Value> {
    return PagingImpl(
        scope = coroutineScope,
        context = context,
        pagingStart = pagingStart,
        initialKey = initialKey,
        config = config,
        load = load,
    )
}