package com.module.mine.api.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
internal data class PersonResponse(
    val id: Int?,
    val uuid: String?,
    val cover: String?,
    val avatar: String?,
    val nickname: String?,
    val sex: Int?,
    @SerializedName("birth_day")
    val birthDay: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    val introduce: String?,
    @SerializedName("follow_num")
    val followNum: Int?,
    @SerializedName("fans_num")
    val fansNum: Int?,
    @SerializedName("is_follow")
    val isFollow: Int?,
    val top3: List<String?>?
)