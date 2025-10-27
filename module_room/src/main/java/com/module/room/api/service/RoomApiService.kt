package com.module.room.api.service

import com.module.basic.api.data.request.PagingRequest
import com.module.basic.api.data.request.BaseRequest
import com.module.basic.api.data.response.*
import com.module.room.api.data.request.FollowRoomRequest
import com.module.room.api.data.request.RoomCreateOrEditRequest
import com.module.room.api.data.response.CreateRoomResponse
import com.module.room.api.data.response.FollowRoomResponse
import com.module.room.api.data.response.RoomCheckResponse
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

internal interface RoomApiService {

    /**
     * 关注的房间
     */
    @POST("index/follow")
    suspend fun followRoom(@Body request: BaseRequest = BaseRequest()): BaseResponse<FollowRoomResponse>

    /**
     * 关注的房间 加载更多
     */
    @POST("index/follow-rooms")
    suspend fun followRoomPage(@Body request: FollowRoomRequest): BaseResponse<BasePagingResponse<RoomResponse>>

    /**
     * 推荐的房间
     */
    @POST("index/recommend")
    suspend fun recommendRoom(@Body request: PagingRequest): BaseResponse<BasePagingResponse<RoomResponse>>

    /**
     * 创建房间
     */
    @POST("room/create-room")
    suspend fun roomCreate(@Body request: RoomCreateOrEditRequest): BaseResponse<CreateRoomResponse>

    /**
     * 能不能创建房间？
     */
    @POST("room/check")
    suspend fun roomCheck(@Body request: BaseRequest = BaseRequest()): BaseResponse<RoomCheckResponse>
    /**
     * 修改房间
     */
    @POST("room/update-room-info")
    suspend fun roomEdit(@Body request: RoomCreateOrEditRequest): BaseResponse<Unit>
    /**
     * 我关注的房间
     */
    @POST("user/follow-room-list")
    suspend fun myFollowRoomList(@Body request: PagingRequest): BaseResponse<BasePagingResponse<RoomResponse>>

    /**
     * 我管理的房间
     */
    @POST("user/manage-room-list")
    suspend fun myManageRoomList(@Body request: PagingRequest): BaseResponse<BasePagingResponse<RoomResponse>>

    /**
     * 隐藏房间
     */
    @POST("user/hide-room")
    suspend fun hideRoom(@Body request: BaseRequest = BaseRequest()): BaseResponse<Unit>

    /**
     * 显示房间
     */
    @POST("user/show-room")
    suspend fun showRoom(@Body request: BaseRequest = BaseRequest()): BaseResponse<Unit>

}