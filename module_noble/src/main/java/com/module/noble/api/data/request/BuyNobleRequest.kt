package com.module.noble.api.data.request

import androidx.annotation.Keep
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class BuyNobleRequest(
    val level: Int
) : BaseRequest()
