package com.module.agent.api.data.request

import androidx.annotation.Keep
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class AgentInviteRequest(
    val uid: String
) : BaseRequest()