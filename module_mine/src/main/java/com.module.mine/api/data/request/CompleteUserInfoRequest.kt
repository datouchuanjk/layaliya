package com.module.mine.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class CompleteUserInfoRequest(
    val avatar: String?,
    val nickname: String?,
    val sex: Int?,
    val language: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("birth_day")
    val birthDay: String?,
) : BaseRequest()