package com.module.noble.api.data.response

import androidx.annotation.Keep

@Keep
internal  data class FindUserResponse(
    val nickname: String?,
    val avatar: String?,
    val id: String?
)
