package com.module.room.api.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class CreateRoomResponse(
    @SerializedName("room_info")
    val roomInfo: RoomInfo?,

) {
    @Keep
    data class RoomInfo(
        val id: String?,
    )
}