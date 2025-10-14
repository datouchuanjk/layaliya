package com.module.agent.api.data.request

import com.module.basic.api.data.request.BaseRequest

internal data class CoinMerchantSendRequest(
    val uid: String,
    val num: Int,
) : BaseRequest()