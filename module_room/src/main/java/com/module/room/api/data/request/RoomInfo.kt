package com.module.room.api.data.request

import com.google.gson.annotations.SerializedName

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
        val userNum: String?,
        @SerializedName("is_follow")
        val isFollow: Int?,
        val type: Int?
    )