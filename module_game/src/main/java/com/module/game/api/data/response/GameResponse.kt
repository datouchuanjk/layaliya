package com.module.game.api.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class GameResponse(
    @SerializedName("agent_id")
    val agentId: Int?,
    val id: Int?,
    val name: String?,
    val path: String?,
    val pic: String?,
    val type: Int?
)