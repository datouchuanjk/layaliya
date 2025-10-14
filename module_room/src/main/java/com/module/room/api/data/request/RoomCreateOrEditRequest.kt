package com.module.room.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class RoomCreateOrEditRequest(
    val init: Int,
    val cover:String,
    val name:String,
    val introduce:String,
    @SerializedName("country_code")
    val countryCode:String,
    val language:String,
    val type:String,
    val mode:String,
): BaseRequest()