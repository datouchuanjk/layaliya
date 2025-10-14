package com.module.mine.api.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class PersonDynamicResponse(
    val id: Int,
    val uid: Int?,
    val content: String?,
    @SerializedName("create_time")
    val createTime: String?,
    val nickname: String?,
    val avatar: String?,
    @SerializedName("praise_num")
    val praiseNum: Int =0 ,
    @SerializedName("comment_num")
    val commentNum: Int=0,
    val images: List<String?>?,
    @SerializedName("im_praise")
    val imPraise: Int?
)