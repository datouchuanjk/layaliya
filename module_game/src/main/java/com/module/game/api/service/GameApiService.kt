package com.module.game.api.service

import com.module.basic.api.data.request.BaseRequest
import com.module.basic.api.data.response.BasePagingResponse
import com.module.basic.api.data.response.BaseResponse
import com.module.game.api.data.request.GameListRequest
import com.module.game.api.data.response.GameResponse
import retrofit2.http.Body
import retrofit2.http.POST


internal interface GameApiService {


    @POST("game/index")
    suspend fun getGameList(@Body request: BaseRequest = BaseRequest()): BaseResponse<BasePagingResponse<GameResponse>>

    @POST("game/room")
    suspend fun getRoomGameList(@Body request: BaseRequest = BaseRequest()): BaseResponse<BasePagingResponse<GameResponse>>
}