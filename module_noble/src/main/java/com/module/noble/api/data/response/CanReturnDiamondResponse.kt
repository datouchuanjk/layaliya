package com.module.noble.api.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class CanReturnDiamondResponse(
    val diamond:Int,
    @SerializedName("is_noble")
    val isNoble:Int
)