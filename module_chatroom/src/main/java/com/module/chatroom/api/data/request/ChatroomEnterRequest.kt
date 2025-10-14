package com.module.chatroom.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class ChatroomEnterRequest(
    @SerializedName("room_id")
    val roomId: String,
    @SerializedName("is_mysterious_person")
    val isMysteriousPerson: Int,
): BaseRequest()
