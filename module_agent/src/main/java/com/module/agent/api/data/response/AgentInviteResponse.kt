package com.module.agent.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class AgentInviteResponse(
    @SerializedName("answer_time")
    val answerTime: Int?,
    @SerializedName("cancel_time")
    val cancelTime: Int?,
    @SerializedName("create_time")
    val createTime: String?,
    val id: Int,
    val nickname: String?,
    val status: Int?,
    @SerializedName("status_text")
    val statusText: String?,
    val uid: Int?
)