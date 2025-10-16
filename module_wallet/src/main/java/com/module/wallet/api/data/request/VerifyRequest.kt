package com.module.wallet.api.data.request

import com.google.gson.annotations.SerializedName

data class VerifyRequest(
    @SerializedName("purchase_token")
    val purchaseToken: String,
    @SerializedName("order_num")
    val orderNum: String
)
