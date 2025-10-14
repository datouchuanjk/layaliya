package com.module.gift.api.data.request

import androidx.annotation.*
import com.google.gson.annotations.SerializedName

@Keep
internal data class SendGiftRequest(
    @SerializedName("room_id")
    val roomId: String = "0",
    @SerializedName("receive_uid")
    val receiveUid: String,
    @SerializedName("gift_id")
    val giftId: Int,
    val num: Int,
)
