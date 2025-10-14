package com.module.chatroom.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class ChatroomMutedRequest(
    @SerializedName("room_id")
    val roomId: String,
    val uid: String,
    val type: Int  //禁言类型(1:10分钟、2:30分钟、3:60分钟、4:24小时、5:永久)
)
