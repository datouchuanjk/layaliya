package com.helper.develop.paging

import androidx.annotation.IntRange

data class PagingConfig(
    val pageSize: Int = 20,
    @IntRange(from = 1)
    val prefetchDistance: Int = 1
) {
    init {
        require(prefetchDistance >= 1)
    }
}
