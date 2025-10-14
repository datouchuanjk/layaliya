package com.module.agent.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class AgentInfoResponse(
    val id: Int?,
    val nickname: String?,
    @SerializedName("receive_account")
    val receiveAccount: String?,
    @SerializedName("receive_method")
    val receiveMethod: Int?,
    @SerializedName("receive_name")
    val receiveName: String?,
    @SerializedName("wait_confirm")
    val waitConfirm: Int?
) {
    val displayReceiveMethod
        get() = when (receiveMethod) {
            1 -> "USDT"
            2 -> "Paypal"
            3 -> "payoneer"
            else -> "Unit"
        }
}