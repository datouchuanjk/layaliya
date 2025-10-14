package com.module.noble.api.service


import com.module.basic.api.data.request.PagingRequest
import com.module.basic.api.data.request.BaseRequest
import com.module.basic.api.data.response.BasePagingResponse
import com.module.basic.api.data.response.BaseResponse
import com.module.noble.api.data.request.BuyNobleRequest
import com.module.noble.api.data.request.FindUserRequest
import com.module.noble.api.data.request.GiveNobleRequest
import com.module.noble.api.data.response.BuyOrGiveNobleRecordResponse
import com.module.noble.api.data.response.CanReturnDiamondResponse
import com.module.noble.api.data.response.FindUserResponse
import com.module.noble.api.data.response.NobleResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface NobleApiService {

    /**
     * 贵族信息
     */
    @POST("config/noble")
    suspend fun nobleInfo(@Body request: BaseRequest = BaseRequest()): BaseResponse<List<NobleResponse>>

    /**
     * 购买贵族
     */
    @POST("noble/buy")
    suspend fun buyNoble(@Body request: BuyNobleRequest): BaseResponse<Unit>

    /**
     * 赠送贵族
     */
    @POST("noble/send")
    suspend fun giveNoble(@Body request: GiveNobleRequest): BaseResponse<Unit>

    /**
     * 通过uid 找到需要赠送的人
     */
    @POST("noble/find-user")
    suspend fun findUser(@Body request: FindUserRequest): BaseResponse<FindUserResponse>

    /**
     * 获取我有多少可以领取的砖石
     */
    @POST("noble/return-diamond")
    suspend fun canReceiveDiamond(@Body request: BaseRequest = BaseRequest()): BaseResponse<CanReturnDiamondResponse>

    /**
     * 领取砖石
     */
    @POST("noble/return-diamond")
    suspend fun receiveDiamond(@Body request: BaseRequest = BaseRequest()): BaseResponse<Unit>

    /**
     * 购买/赠送记录
     */
    @POST("noble/diamond-record")
    suspend fun buyOrGiveNobleList(@Body request: PagingRequest): BaseResponse<BasePagingResponse<BuyOrGiveNobleRecordResponse>>
}