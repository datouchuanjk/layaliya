package com.module.noble.api.data.request

import androidx.annotation.Keep
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class GiveNobleRequest(
    val level: Int,
    val uid: String,
) : BaseRequest()
