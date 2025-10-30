package com.module.login.api.service

import androidx.annotation.Keep
import com.module.basic.api.data.request.BaseRequest
import com.module.basic.api.data.response.BaseResponse
import com.module.login.api.data.request.GoogleLoginRequest
import com.module.login.api.data.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

@Keep
internal interface LoginApiService {

    /**
     * 登录
     */
    @POST("login/guest")
    suspend fun login(@Body request: BaseRequest = BaseRequest()): BaseResponse<LoginResponse>

    /**
     * 登录
     */
    @POST("login/google")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): BaseResponse<LoginResponse>
}