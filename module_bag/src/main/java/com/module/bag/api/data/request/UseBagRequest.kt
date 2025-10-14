package com.module.bag.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class UseBagRequest(
    @SerializedName("prop_id")
    val propId: String,
)
