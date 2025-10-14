package com.module.bag.api.service

import com.module.bag.api.data.request.UseBagRequest
import com.module.basic.api.data.request.*
import com.module.basic.api.data.response.*
import com.module.bag.api.data.response.*
import retrofit2.http.Body
import retrofit2.http.POST


internal interface BagApiService {

    @POST("user/my-bag")
    suspend fun bagInfo(@Body request: BaseRequest = BaseRequest()): BaseResponse<BagResponse>

    @POST("user/use-prop")
    suspend fun use(@Body request: UseBagRequest): BaseResponse<Unit>
}