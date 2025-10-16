package com.module.wallet.api.data.request

import com.module.basic.api.data.request.BaseRequest

data class DiamondListRequest(
    val type: Int = 1
) : BaseRequest()
