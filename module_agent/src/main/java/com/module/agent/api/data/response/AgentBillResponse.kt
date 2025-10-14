package com.module.agent.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class AgentBillResponse(
    val coins: String?,
    val nickname: String?,
    val status: Int?,
    @SerializedName("total_coins")
    val totalCoins: String?,
    val uid: Int
)