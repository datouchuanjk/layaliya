package com.module.basic.api.data.response

import com.google.gson.annotations.*

data class RoomResponse(
    @SerializedName("follow_room_id")
    val followRoomId: Int?,
    val uid: Int,
    val isOpen: Int,
    val id: Int?,
    val uuid: String?,
    val name: String?,
    val cover: String?,
    @SerializedName("hot_val")
    val hotVal: Int = 0,
    @SerializedName("user_uuid")
    val userUuid: Int?,
    @SerializedName("mike_user_num")
    val mikeUserNum: Int?,
    val avatars: List<String>?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("is_show")
    val isShow: Int?,
)