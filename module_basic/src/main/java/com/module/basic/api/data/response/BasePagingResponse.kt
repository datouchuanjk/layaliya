package com.module.basic.api.data.response

import com.google.gson.annotations.SerializedName


class BasePagingResponse<T>(
    @SerializedName("page_size")
    val pageSize: Int = 0,
    @SerializedName("current_page")
    val currentPage: Int = 0,
    val total: Int = 0,
    val list: List<T> = emptyList()
)


