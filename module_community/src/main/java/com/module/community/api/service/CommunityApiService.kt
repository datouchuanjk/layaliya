package com.module.community.api.service

import com.module.basic.api.data.request.*
import com.module.basic.api.data.response.BasePagingResponse
import com.module.basic.api.data.response.BaseResponse
import com.module.community.api.data.request.CommentLikeRequest
import com.module.community.api.data.request.CommentListRequest
import com.module.community.api.data.request.CommunityLikeRequest
import com.module.community.api.data.request.CommunityListRequest
import com.module.community.api.data.request.PostCommentRequest
import com.module.community.api.data.request.PostCommunityRequest
import com.module.community.api.data.response.CommentResponse
import com.module.community.api.data.response.CommunityResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface CommunityApiService {


    /**
     * 发布社区动态
     */
    @POST("zone/add")
    suspend fun poshCommunity(@Body request: PostCommunityRequest): BaseResponse<Unit>

    /**
     * 社区动态列表
     */
    @POST("zone/index")
    suspend fun communityList(@Body request: CommunityListRequest): BaseResponse<BasePagingResponse<CommunityResponse>>

    /**
     * 点赞
     */
    @POST("zone/praise")
    suspend  fun dynamicLike(@Body request: CommunityLikeRequest): BaseResponse<Unit>
    /**
     * 点赞
     */
    @POST("zone/un-praise")
    suspend  fun dynamicUnlike(@Body request: CommunityLikeRequest): BaseResponse<Unit>

    /**
     * 关注用户
     */
    @POST("user/follow-user")
    suspend fun followUser(@Body request: UidRequest): BaseResponse<Unit>

    /**
     * 取消关注用户
     */
    @POST("user/un-follow-user")
    suspend fun unfollowUser(@Body request: UidRequest): BaseResponse<Unit>


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