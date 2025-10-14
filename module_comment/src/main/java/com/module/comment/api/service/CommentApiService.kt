package com.module.comment.api.service

import com.module.basic.api.data.response.BasePagingResponse
import com.module.basic.api.data.response.BaseResponse
import com.module.comment.api.data.request.CommentLikeRequest
import com.module.comment.api.data.request.CommentListRequest
import com.module.comment.api.data.request.PostCommentRequest
import com.module.comment.api.data.response.CommentResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface CommentApiService {

    /**
     * 发布一个评论
     */
    @POST("zone/comment")
   suspend fun postComment(@Body request: PostCommentRequest): BaseResponse<CommentResponse>

    /**
     * 评论列表
     */
    @POST("zone/comment-list")
    suspend fun commentList(@Body request: CommentListRequest): BaseResponse<BasePagingResponse<CommentResponse>>

    /**
     * 点赞
     */
    @POST("zone/comment-praise")
    suspend fun commentLike(@Body request: CommentLikeRequest): BaseResponse<Unit>

    /**
     * 点赞
     */
    @POST("zone/un-comment-praise")
    suspend fun commentUnlike(@Body request: CommentLikeRequest): BaseResponse<Unit>
}