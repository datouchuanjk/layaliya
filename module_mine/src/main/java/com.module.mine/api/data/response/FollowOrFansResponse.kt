package com.module.mine.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class FollowOrFansResponse(
    val avatar: String?,
    @SerializedName("charm_level")
    val charmLevel: Int?,
    val nickname: String?,
    @SerializedName("noble_level")
    val nobleLevel: Int=0,
    val status: Int?, //状态(0对方关注我、1我关注对方、2互相关注)
    val uid: Int,
    @SerializedName("wealth_level")
    val wealthLevel: Int?
)