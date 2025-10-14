package com.module.wealth.api.service

import com.module.basic.api.data.request.BaseRequest
import com.module.basic.api.data.response.BaseResponse
import com.module.wealth.api.data.response.WealthLevelResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface WealthApiService {
    /**
     * 获取财富信息
     */
    @POST("config/wealth")
    suspend fun wealthLevel(@Body request: BaseRequest = BaseRequest()): BaseResponse<WealthLevelResponse>
}