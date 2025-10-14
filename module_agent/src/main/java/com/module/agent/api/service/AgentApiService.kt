package com.module.agent.api.service

import com.module.basic.api.data.request.PagingRequest
import com.module.basic.api.data.request.BaseRequest
import com.module.basic.api.data.response.BasePagingResponse
import com.module.basic.api.data.response.BaseResponse
import com.module.agent.api.data.request.AgentInviteRequest
import com.module.agent.api.data.request.WeekRangeRequest
import com.module.agent.api.data.request.CoinMerchantSendRequest
import com.module.agent.api.data.response.AdminResponse
import com.module.agent.api.data.response.AgentBillResponse
import com.module.agent.api.data.response.AgentIdolResponse
import com.module.agent.api.data.response.AgentInfoResponse
import com.module.agent.api.data.response.AgentInviteResponse
import com.module.agent.api.data.response.BDResponse
import com.module.agent.api.data.response.CoinDetailResponse
import com.module.basic.api.data.request.UidRequest
import com.module.basic.api.data.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AgentApiService {

    /**
     * 获取代理信息
     */
    @POST("agent/info")
    suspend fun getAgentInfo(@Body request: BaseRequest = BaseRequest()): BaseResponse<AgentInfoResponse>

    /**
     * 代理邀请
     */
    @POST("agent/invite-anchor")
    suspend fun agentInvite(@Body request: AgentInviteRequest): BaseResponse<Unit>

    /**
     * 代理邀请记录
     */
    @POST("agent/inviting-record")
    suspend fun agentInviteRecord(@Body request: PagingRequest): BaseResponse<BasePagingResponse<AgentInviteResponse>>

    /**
     * 代理主播列表
     */
    @POST("agent/anchor-list")
    suspend fun agentIdolRecord(@Body request: PagingRequest): BaseResponse<BasePagingResponse<AgentIdolResponse>>

    /**
     * 代理账单
     */
    @POST("agent/bill-list")
    suspend fun agentBillRecord(@Body request: PagingRequest): BaseResponse<BasePagingResponse<AgentBillResponse>>

    /**
     * 查询用户 通过uid B商
     */
    @POST("bd/search-user")
    suspend fun findUserByUid(@Body request: UidRequest): BaseResponse<UserResponse>

    /**
     * 赠送B商
     */
    @POST("bd/send-diamond")
    suspend fun sendDiamondWithCoinMerchant(@Body request: CoinMerchantSendRequest): BaseResponse<Unit>

    /**
     * B商 收益列表
     */
    @POST("user/diamond-change-log")
    suspend fun coinDetailList(@Body request: WeekRangeRequest): BaseResponse<BasePagingResponse<CoinDetailResponse>>

    /**
     * admin 收益列表
     */
    @POST("bd/admin-incoming")
    suspend fun adminIncomingList(@Body request: WeekRangeRequest): BaseResponse<BasePagingResponse<AdminResponse>>

    /**
     * admin 收益列表
     */
    @POST("bd/bd-incoming")
    suspend fun bdIncomingList(@Body request: WeekRangeRequest): BaseResponse<BasePagingResponse<BDResponse>>
}