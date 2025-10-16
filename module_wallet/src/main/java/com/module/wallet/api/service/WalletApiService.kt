package com.module.wallet.api.service

import com.module.basic.api.data.response.BaseResponse
import com.module.wallet.api.data.request.BuyRequest
import com.module.wallet.api.data.request.DiamondListRequest
import com.module.wallet.api.data.request.VerifyRequest
import com.module.wallet.api.data.response.BuyResponse
import com.module.wallet.api.data.response.DiamondListResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface WalletApiService {

    /**
     * 充值商品列表
     */
    @POST("recharge-goods/index")
    suspend fun getDiamondList(@Body request: DiamondListRequest = DiamondListRequest()): BaseResponse<List<DiamondListResponse>>

    /**
     * 购买商品
     */
    @POST("recharge-order/create-order")
    suspend fun buy(@Body request: BuyRequest): BaseResponse<BuyResponse>

    /**
     * 验证商品
     */
    @POST("recharge-order/verify-google-payment")
    suspend fun verify(@Body request: VerifyRequest): BaseResponse<BuyResponse>
}