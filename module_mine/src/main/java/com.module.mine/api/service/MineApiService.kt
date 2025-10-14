package com.module.mine.api.service

import com.module.basic.api.data.request.UidRequest
import com.module.basic.api.data.response.BasePagingResponse
import com.module.basic.api.data.response.BaseResponse
import com.module.mine.api.data.request.CompleteUserInfoRequest
import com.module.mine.api.data.request.EditUserInfoRequest
import com.module.mine.api.data.request.FollowOrFansRequest
import com.module.mine.api.data.request.PersonDynamicLikeRequest
import com.module.mine.api.data.request.PersonDynamicRequest
import com.module.mine.api.data.response.FollowOrFansResponse
import com.module.mine.api.data.response.PersonDynamicResponse
import com.module.mine.api.data.response.PersonResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface MineApiService {

    /**
     * 关注列表
     */
    @POST("user/follow-list")
    suspend fun followList(@Body request: FollowOrFansRequest): BaseResponse<BasePagingResponse<FollowOrFansResponse>>

    /**
     * 粉丝列表
     */
    @POST("user/fans")
    suspend fun fansList(@Body request: FollowOrFansRequest): BaseResponse<BasePagingResponse<FollowOrFansResponse>>

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
     * 用户主页
     */
    @POST("user/other-info")
    suspend fun personInfo(@Body request: UidRequest): BaseResponse<PersonResponse>

    /**
     * 用户主页动态列表
     */
    @POST("user/zone")
    suspend fun personDynamicList(@Body request: PersonDynamicRequest): BaseResponse<BasePagingResponse<PersonDynamicResponse>>

    /**
     * 点赞
     */
    @POST("zone/praise")
    suspend fun dynamicLike(@Body request: PersonDynamicLikeRequest): BaseResponse<Unit>

    /**
     * 点赞
     */
    @POST("zone/un-praise")
    suspend fun dynamicUnlike(@Body request: PersonDynamicLikeRequest): BaseResponse<Unit>

    /**
     * 完善资料
     */
    @POST("user/complete-info")
    suspend fun completeUserInfo(@Body request: CompleteUserInfoRequest): BaseResponse<Unit>

    /**
     * 修改资料
     */
    @POST("user/update-info")
    suspend fun editUserInfo(@Body request: EditUserInfoRequest): BaseResponse<Unit>



}