package com.module.mine.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class PersonDynamicLikeRequest(
    @SerializedName("zone_id")
    val zoneId: String
) : BaseRequest()
