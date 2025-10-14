package com.module.basic.api.service

import com.module.basic.api.data.request.BaseRequest
import com.module.basic.api.data.response.BaseResponse
import com.module.basic.api.data.response.ConfigResponse
import com.module.basic.api.data.response.UploadConfigResponse
import com.module.basic.api.data.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 通用的一些接口定义在这里
 */
interface BasicApiService {

    /**
     * 获取配置
     */
    @POST("config/config")
    suspend fun config(@Body request: BaseRequest = BaseRequest()): BaseResponse<ConfigResponse>

    /**
     * 获取个人信息
     */
    @POST("user/info")
    suspend fun user(@Body request: BaseRequest = BaseRequest()): BaseResponse<UserResponse>

    /**
     * 获取上传图片的key
     */
    @POST("config/get-upload-config")
    suspend fun uploadConfig(@Body request: BaseRequest = BaseRequest()): BaseResponse<UploadConfigResponse>

    @POST("heartbeat/index")
    suspend fun hearBeat(@Body request: BaseRequest = BaseRequest()): Any
}