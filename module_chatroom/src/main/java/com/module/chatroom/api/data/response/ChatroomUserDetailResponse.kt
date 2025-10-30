package com.module.chatroom.api.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
internal  data class ChatroomUserDetailResponse(
    val id: String?,
    val uuid: String?,
    val nickname: String?,
    val avatar: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("wealth_level")
    val wealthLevel: String?,
    @SerializedName("charm_level")
    val charmLevel: String?,
    @SerializedName("noble_level")
    val nobleLevel: String?,
    @SerializedName("is_follow")
    val isFollow: String?,
    @SerializedName("is_muted")
    val isMuted: String?,
    val role: Int?,
    @SerializedName("yunxin_accid")
    val yunxinAccid: String?,
    @SerializedName("is_mysterious_person")
    val isMysteriousPerson:Int?,
){
    val isMaster get() = role == 1 && isMysteriousPerson != 1
    val isAdmin get() = role == 2 && isMysteriousPerson != 1
    val isMasterOrAdmin get() = isMaster || isAdmin

    val routeLevel get() = if(isMysteriousPerson==1)4 else if (isMaster) 3 else if (isAdmin) 2 else 1
}
