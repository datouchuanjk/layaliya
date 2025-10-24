package com.module.basic.api.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class SearchUserResponse(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("online_status")
    val onlineStatus: Int?,
    @SerializedName("uuid")
    val uuid: Int?
)