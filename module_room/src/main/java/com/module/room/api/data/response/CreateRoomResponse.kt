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
        val cover: String?,
        val name: String?,
        val introduce: String?,
        @SerializedName("mike_num")
        val mikeNum: Int?,
        @SerializedName("country_code")
        val countryCode: String?,
        val language: String?,
        @SerializedName("user_num")
        val userNum: Int?,
        @SerializedName("is_follow")
        val isFollow: Int?,
        @SerializedName("yunxin_roomid")
        val yunxinRoomId: String?,
        @SerializedName("rtc_token")
        val rtcToken: String?,
        @SerializedName("rtc_channel_name")
        val rtcChannelName: String?,
        @SerializedName("rtc_uid")
        val rtcUid: String?,
        val type: Int?,
        val uuid: Int?,
        @SerializedName("hot_val")
        val hotVal: String?,
    ) {
        val displayUserNum get() = if ((userNum ?: 0) > 999) "999+" else userNum.toString()
    }
}