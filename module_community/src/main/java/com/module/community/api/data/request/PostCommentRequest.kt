package com.module.community.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class PostCommentRequest(
    @SerializedName("zone_id")
    val  zoneId:String,
    val  content:String,
    @SerializedName("comment_id")
    val  commentId:String,
)
