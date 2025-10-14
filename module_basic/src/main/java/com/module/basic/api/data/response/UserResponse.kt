package com.module.basic.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserResponse(
    val cover: String?,
    val avatar: String?,
    @SerializedName("birth_day")
    val birthDay: String?,
    @SerializedName("charm_exp")
    val charmExp: Int?,
    @SerializedName("charm_level")
    val charmLevel: Int?,
    val coins: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    val diamond: String?,
    @SerializedName("fans_num")
    val fansNum: Int?,
    @SerializedName("follow_num")
    val followNum: Int?,
    @SerializedName("follow_room_num")
    val followRoomNum: Int?,
    val id: Int?,
    val introduce: String?,
    @SerializedName("is_agent")
    val isAgent: Int?,
    @SerializedName("is_anchor")
    val isAnchor: Int?,
    @SerializedName("is_bd")
    val isBd: Int?,
    @SerializedName("is_super")
    val isSuper: Int?,
    val nickname: String?,
    @SerializedName("noble_expire_time")
    val nobleExpireTime: Int?,
    @SerializedName("noble_level")
    val nobleLevel: Int?,
    @SerializedName("room_info")
    val roomInfo: RoomResponse?,
    val sex: Int?,
    val uuid: String?,
    @SerializedName("wealth_exp")
    val wealthExp: Int?,
    @SerializedName("wealth_level")
    val wealthLevel: Int?,
    val language: String?,
    @SerializedName("is_b_business")
    val isBBusiness: String?,
    @SerializedName("im_account")
    val imAccount: String?,
    @SerializedName("im_token")
    val imToken: String?,
    @SerializedName("is_complete")
    val isComplete: String
)