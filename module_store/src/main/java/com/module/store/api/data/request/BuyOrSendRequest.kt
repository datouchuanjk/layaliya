package com.module.store.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class BuyOrSendRequest(
    @SerializedName("goods_id")
    val goodsId: String,
    val uid: String = "",
)