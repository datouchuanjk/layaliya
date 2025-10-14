package com.module.agent.api.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class BDResponse(
    val uid: String?,
    val nickname: String?,
    @SerializedName("anchor_num")
    val anchorNum: String?,
    @SerializedName("anchor_incoming")
    val anchorIncoming: String?,
    @SerializedName("agent_incoming")
    val agentIncoming: String?,
)
