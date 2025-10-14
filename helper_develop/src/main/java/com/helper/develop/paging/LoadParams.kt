package com.helper.develop.paging

data class LoadParams<Key>(
    val key: Key?,
    val pageSize: Int,
)
