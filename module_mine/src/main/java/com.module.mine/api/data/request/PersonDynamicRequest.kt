package com.module.mine.api.data.request

import androidx.annotation.Keep
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class PersonDynamicRequest(
    val uid: String,
    val page: Int
) : BaseRequest()
