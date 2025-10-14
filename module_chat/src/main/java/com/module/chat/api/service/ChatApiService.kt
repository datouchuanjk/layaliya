package com.module.chat.api.service

import com.module.chat.api.data.request.AcceptInviteRequest
import com.module.basic.api.data.response.*
import retrofit2.http.Body
import retrofit2.http.POST


internal interface ChatApiService {

    @POST("user/accept-invite")
    suspend fun acceptInvite(@Body request: AcceptInviteRequest): BaseResponse<Unit>

}