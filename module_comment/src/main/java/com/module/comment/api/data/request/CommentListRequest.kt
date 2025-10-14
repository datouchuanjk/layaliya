package com.module.comment.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class CommentListRequest(
    val page: Int,
    @SerializedName("zone_id")
    val zoneId: String
):BaseRequest()
