package com.module.chatroom.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class ChatroomSeatRequest(
    @SerializedName("seat_id")
    val seatId: String,
   @SerializedName("room_id")
    val roomId: String,
)
