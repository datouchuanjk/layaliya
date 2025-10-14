package com.module.room.api.data.request

import androidx.annotation.Keep
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class FollowRoomRequest(
    val type: String,
    val page: Int
) : BaseRequest()