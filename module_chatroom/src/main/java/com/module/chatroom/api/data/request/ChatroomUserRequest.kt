package com.module.chatroom.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class ChatroomUserRequest(
    val uid: String,
    @SerializedName("room_id")
    val roomId: String,
)
