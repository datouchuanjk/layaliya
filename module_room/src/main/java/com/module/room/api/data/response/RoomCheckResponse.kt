package com.module.room.api.data.response

import androidx.annotation.Keep

@Keep
internal data class RoomCheckResponse(
    val type: Int,
    val roomInfo : CreateRoomResponse.RoomInfo?
)
