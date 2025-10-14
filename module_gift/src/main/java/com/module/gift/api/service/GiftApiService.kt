package com.module.gift.api.service

import com.module.basic.api.data.request.*
import com.module.basic.api.data.response.*
import com.module.gift.api.data.request.SendGiftRequest
import com.module.gift.api.data.response.*
import retrofit2.http.Body
import retrofit2.http.POST

internal interface GiftApiService {

    @POST("gift/index")
    suspend fun getGiftInfo(@Body request: BaseRequest = BaseRequest()): BaseResponse<GiftResponse>

    @POST("gift/send-gift")
    suspend   fun sendGift(@Body request: SendGiftRequest):BaseResponse<Unit>
}