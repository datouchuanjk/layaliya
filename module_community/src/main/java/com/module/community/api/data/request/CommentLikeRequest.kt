package com.module.community.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class CommentLikeRequest(
    @SerializedName("comment_id")
    val commentId: String
) : BaseRequest()
