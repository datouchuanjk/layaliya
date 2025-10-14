package com.module.comment.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class CommentResponse(
    @SerializedName("answer_nickname")
    val answerNickname: String?,
    @SerializedName("answer_uid")
    val answerUid: Int?,
    val avatar: String?,
    @SerializedName("comment_id")
    val commentId: Int?,
    val content: String?,
    @SerializedName("create_time")
    val createTime: String?,
    val id: Int,
    @SerializedName("im_praise")
    val imPraise: Int?,
    val nickname: String?,
    @SerializedName("praise_num")
    val praiseNum: Int =0,
    val uid: Int?,
    @SerializedName("zone_id")
    val zoneId: Int?
) {
    val displayContent get() = if (answerNickname.isNullOrEmpty()) content.orEmpty() else "@${answerNickname} $content"
}