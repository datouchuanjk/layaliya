package com.module.chatroom.api.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ChatroomInfoResponse(
    @SerializedName("room_info")
    val roomInfo: RoomInfo?,
    @SerializedName("mike_info")
    val mikeInfo: List<MikeInfo?>?,
    @SerializedName("user_info")
    val userInfo: UserInfo?,
    val notice: Notice?
) {
    @Keep
    data class RoomInfo(
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
        val type: Int?
    ){
        val displayUserNum get() = if((userNum?:0)>999) "999+" else  userNum.toString()
    }

    @Keep
    data class MikeInfo(
        val id: Int?,
        val index: Int?,
        val uid: Int?,
        val status: Int?,
        val nickname: String?,
        val avatar: String?,
        @SerializedName("rtc_uid")
        val rtcUid: String?,
        val emojiId: String?
    )

    @Keep
    data class Notice(
        val msg: String?,
        val tips: String?,
        val car: String?
    )
    @Keep
    data class UserInfo(
        val role: Int?, //(0用户、1房主、2管理)
        @SerializedName("is_muted")
        val isMuted: Int?,
        @SerializedName("muted_last_time")
        val mutedLastTime: Int?,
        @SerializedName("can_send_image")
        val canSendImage: Int?,
        @SerializedName("room_member_id")
        val roomMemberId: Int?,
        @SerializedName("is_mysterious_person")
        val isMysteriousPerson:Int?,
    ) {
//        val isMaster get() = role == 1
        val isMaster get() =true
        val isAdmin get() = role == 2
        val isMasterOrAdmin get() = isMaster || isAdmin
    }
}