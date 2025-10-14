package com.module.agent.api.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class CoinDetailResponse(
    val time: String?,
    val action: String?,
    @SerializedName("change_num")
    val changeNum: String?,
    @SerializedName("last_num")
    val lastNum: String?,
    val remark: String?
)