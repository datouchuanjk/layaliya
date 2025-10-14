package com.module.basic.api.data.response

import com.module.basic.api.*


class BaseResponse<T>(
    val code: Int = 0,
    val message: String = "",
    val data: T? = null
) {
    val isSuccessful get() = code == 200

    fun checkAndGet() = if (isSuccessful) {
        data
    } else {
        throw ApiException(
            code = code,
            message = message
        )
    }
}


