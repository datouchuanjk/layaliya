package com.module.community.api.data.request

import androidx.annotation.Keep
import com.module.basic.api.data.request.BaseRequest
@Keep
 internal data class PostCommunityRequest(
    val content: String,
    val images: List<String>
) : BaseRequest()
