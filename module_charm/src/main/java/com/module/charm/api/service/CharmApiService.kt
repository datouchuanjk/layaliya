package com.module.charm.api.service

import com.module.basic.api.data.request.*
import com.module.basic.api.data.response.*
import com.module.charm.api.data.response.CharmLevelResponse
import retrofit2.http.*

internal interface CharmApiService {
    /**
     * 获取魅力信息
     */
    @POST("config/charm")
    suspend fun charmLevel(@Body request: BaseRequest = BaseRequest()): BaseResponse<CharmLevelResponse>
}