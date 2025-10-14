package com.module.chatroom.api.service

import com.module.basic.api.data.request.UidRequest
import com.module.basic.api.data.response.*
import com.module.chatroom.api.data.request.ChatroomEnterRequest
import com.module.chatroom.api.data.request.ChatroomMutedRequest
import com.module.chatroom.api.data.request.ChatroomInfoRequest
import com.module.chatroom.api.data.request.ChatroomUserRequest
import com.module.chatroom.api.data.request.ChatroomSeatRequest
import com.module.chatroom.api.data.request.ChatroomUserSeatRequest
import com.module.chatroom.api.data.request.ChatroomListRequest
import com.module.chatroom.api.data.request.ChatroomReportRequest
import com.module.chatroom.api.data.request.ChatroomSearchRequest
import com.module.chatroom.api.data.response.ChatroomUserDetailResponse
import com.module.chatroom.api.data.response.ChatroomInfoResponse
import com.module.chatroom.api.data.response.ChatroomUserResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface ChatroomApiService {

    /**
     * 获取房间信息
     */
    @POST("room/into-room")
    suspend fun getRoomInfo(@Body request: ChatroomEnterRequest): BaseResponse<ChatroomInfoResponse>

    /**
     * 获取房间麦信息信息
     */
    @POST("room/seat-list")
    suspend fun getRoomMikeInfo(@Body request: ChatroomInfoRequest): BaseResponse<List<ChatroomInfoResponse.MikeInfo>>

    /**
     * 退出房间
     */
    @POST("room/exit-room")
    suspend fun exitRoom(@Body request: ChatroomInfoRequest): BaseResponse<Unit>

    /**
     * 踢出房间
     */
    @POST("room/kickout")
    suspend fun kickout(@Body request: ChatroomUserRequest): BaseResponse<Unit>

    /**
     * 房间用户列表
     */
    @POST("room/user-list")
    suspend fun getRoomUserList(@Body request: ChatroomInfoRequest): BaseResponse<BasePagingResponse<ChatroomUserResponse>>

    /**
     * 房间用户列表 key
     */
    @POST("room/user-list")
    suspend fun getRoomUserListByKey(@Body request: ChatroomSearchRequest): BaseResponse<BasePagingResponse<ChatroomUserResponse>>

    /**
     * 房间管理列表
     */
    @POST("room/manage-list")
    suspend fun getRoomAdminList(@Body request: ChatroomInfoRequest): BaseResponse<BasePagingResponse<ChatroomUserResponse>>

    /**
     * 房间禁言列表
     */
    @POST("room/mute-list")
    suspend fun getRoomMuteList(@Body request: ChatroomListRequest): BaseResponse<BasePagingResponse<ChatroomUserResponse>>

    /**
     * 设置管理
     */
    @POST("room/set-manage")
    suspend fun setManager(@Body request: ChatroomUserRequest): BaseResponse<Unit>

    /**
     * 取消管理
     */
    @POST("room/cancel-manage")
    suspend fun unAdmin(@Body request: ChatroomUserRequest): BaseResponse<Unit>


    /**
     * 上麦
     */
    @POST("room/up-seat")
    suspend fun upSeat(@Body request: ChatroomSeatRequest): BaseResponse<Unit>

    /**
     * 下麦
     */
    @POST("room/down-seat")
    suspend fun downSeat(@Body request: ChatroomSeatRequest): BaseResponse<Unit>

    /**
     * 开麦
     */
    @POST("room/open-seat")
    suspend fun openSeat(@Body request: ChatroomSeatRequest): BaseResponse<Unit>

    /**
     * 关麦
     */
    @POST("room/close-seat")
    suspend fun closeSeat(@Body request: ChatroomSeatRequest): BaseResponse<Unit>

    /**
     * 拉人上麦
     */
    @POST("room/up-user-seat")
    suspend fun upUserSeat(@Body request: ChatroomUserSeatRequest): BaseResponse<Unit>

    /**
     * 踢人下麦
     */
    @POST("room/down-user-seat")
    suspend fun downUserSeat(@Body request: ChatroomUserSeatRequest): BaseResponse<Unit>


    /**
     * 禁言某个屌毛
     */
    @POST("room/muted")
    suspend fun silence(@Body request: ChatroomMutedRequest): BaseResponse<Unit>

    /**
     * 取消禁言
     */
    @POST("room/un-muted")
    suspend fun unmuted(@Body request: ChatroomUserRequest): BaseResponse<Unit>

    /**
     * 举报房间
     */
    @POST("report/room")
    suspend fun reportRoom(@Body request: ChatroomReportRequest): BaseResponse<Unit>


    /**
     * 麦上的用户信息
     */
    @POST("room/user-info")
    suspend fun   getMikeUserInfo(@Body request: ChatroomUserRequest): BaseResponse<ChatroomUserDetailResponse>

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
     * 关注房间
     */
    @POST("user/follow-room")
    suspend fun followRoom(@Body request: ChatroomInfoRequest): BaseResponse<Unit>

    /**
     * 取消关注房间
     */
    @POST("user/un-follow-room")
    suspend fun unfollowRoom(@Body request: ChatroomInfoRequest): BaseResponse<Unit>

    /**
     * 能否用神秘人身份进入
     */
    @POST("room/is-mysterious-man")
    suspend fun isMysteriousMan(): BaseResponse<Map<String, Int>>
}