package com.module.chat.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class AcceptInviteRequest(
    @SerializedName("invite_id")
    val inviteId: String,
)
