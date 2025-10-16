package com.module.wallet.api.data.request

import com.google.gson.annotations.SerializedName
import com.module.basic.api.data.request.BaseRequest

data class BuyRequest(
    @SerializedName("recharge_goods_id")
    val rechargeGoodsId: String
): BaseRequest()
