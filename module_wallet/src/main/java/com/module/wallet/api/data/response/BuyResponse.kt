package com.module.wallet.api.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BuyResponse(
    @SerializedName("google_product_id")
    val googleProductId: String?,
    @SerializedName("order_num")
    val orderNum: String?,
    @SerializedName("price")
    val price: String?
)