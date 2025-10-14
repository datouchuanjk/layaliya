package com.module.noble.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class BuyOrGiveNobleRecordResponse(
    @SerializedName("create_time")
    val createTime: String?,
    val id: Int,
    val level: String?,
    @SerializedName("to_uid")
    val toUid: String?,
    val type: Int?,
    @SerializedName("type_text")
    val typeText: String?
)