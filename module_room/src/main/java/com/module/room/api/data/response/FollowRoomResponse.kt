package com.module.room.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.module.basic.api.data.response.*

@Keep
internal data class FollowRoomResponse(
    @SerializedName("room_list_type")
    val roomListType: String?,
    @SerializedName("rooms")
    val rooms: BasePagingResponse<RoomResponse> = BasePagingResponse(),
    @SerializedName("users")
    val users: List<UserResponse>?
)