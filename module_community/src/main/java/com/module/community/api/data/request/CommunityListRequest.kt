package com.module.community.api.data.request

import androidx.annotation.Keep
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class CommunityListRequest(
    val page: Int,
    val type: String
):BaseRequest()
