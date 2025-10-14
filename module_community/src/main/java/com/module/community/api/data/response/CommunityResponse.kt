package com.module.community.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class CommunityResponse(
    val avatar: String?,
    @SerializedName("comment_num")
    val commentNum: Int=0,
    val content: String?,
    @SerializedName("create_time")
    val createTime: String?,
    val id: Int,
    @SerializedName("im_praise")
    val imPraise: Int =0,
    val images: List<String?>?,
    val nickname: String?,
    @SerializedName("praise_num")
    val praiseNum: Int=0,
    val uid: Int?,
    @SerializedName("is_follow")
    val isFollow: Int=0,
)