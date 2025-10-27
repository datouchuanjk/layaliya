package com.module.community.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class CommunityResponse(
    val id: Int,
    val uid: Int?,
    val content: String?,
    @SerializedName("create_time")
    val createTime: String?,
    val nickname: String?,
    val avatar: String?,
    @SerializedName("praise_num")
    val praiseNum: Int=0,
    @SerializedName("comment_num")
    val commentNum: Int=0,
    @SerializedName("im_praise")
    val imPraise: Int =0,
    val images: List<String?>?,
    @SerializedName("is_follow")
    val isFollow: Int=0,
)