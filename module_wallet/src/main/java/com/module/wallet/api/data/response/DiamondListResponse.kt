package com.module.wallet.api.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class DiamondListResponse(
    @SerializedName("desc")
    val desc: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("num")
    val num: Int?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("send_num")
    val sendNum: Int?,
    @SerializedName("three_goods_id")
    val threeGoodsId: String?,
    val isSelected: Boolean = false,
) {
    val displayNum get() = (num ?: 0) + (sendNum ?: 0)
    val isTop get() = topValue>100

    val topValue get() = (displayNum.toFloat() / (num ?: 0)*100).toInt()
}