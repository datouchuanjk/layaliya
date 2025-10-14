package com.module.store.api.service

import com.module.basic.api.data.request.*
import com.module.basic.api.data.response.*
import com.module.store.api.data.request.BuyOrSendRequest
import com.module.store.api.data.response.*
import retrofit2.http.Body
import retrofit2.http.POST


internal interface StoreApiService {


    @POST("goods/index")
    suspend fun storeInfo(@Body request: BaseRequest = BaseRequest()): BaseResponse<StoreResponse>

    @POST("user/buy-goods")
    suspend fun buyOrSend(@Body request: BuyOrSendRequest): BaseResponse<Unit>
}