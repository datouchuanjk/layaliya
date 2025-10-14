package com.module.chatroom.api.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class ChatroomUserResponse(
    val id: Int?,
    @SerializedName("room_id")
    val roomId: Int?,
    val uid: Int,
    @SerializedName("join_time")
    val joinTime: Int?,
    @SerializedName("leave_time")
    val leaveTime: Int?,
    val role: Int?, //(0用户、1房主、2管理)
    @SerializedName("is_muted")
    val isMuted: Int?,
    @SerializedName("muted_type")
    val mutedType: Int?,
    @SerializedName("muted_beign_time")
    val mutedBeignTime: Int?,
    @SerializedName("muted_end_time")
    val mutedEndTime: Int?,
    @SerializedName("is_black")
    val isBlack: Int?,
    @SerializedName("black_type")
    val blackType: Int?,
    @SerializedName("black_begin_time")
    val blackBeginTime: Int?,
    @SerializedName("black_end_time")
    val blackEndTime: Int?,
    val avatar: String?,
    val nickname: String?,
    @SerializedName("yunxin_accid")
    val yunxinAccid: String?,
    @SerializedName("is_mysterious_person")
    val isMysteriousPerson: Int?,
)