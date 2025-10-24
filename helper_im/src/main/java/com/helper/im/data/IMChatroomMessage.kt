package com.helper.im.data

import androidx.annotation.Keep
import com.helper.im.IMHelper
import com.helper.im.util.format
import com.netease.nimlib.sdk.v2.chatroom.model.V2NIMChatroomMessage

fun V2NIMChatroomMessage.transform(body: IMChatroomMessageBody? = null) =
    IMChatroomMessage(this, body = body)

@Keep
data class IMChatroomMessage(
    val v2NIMChatroomMessage: V2NIMChatroomMessage,
    val messageId: String = v2NIMChatroomMessage.messageClientId,
    private val userInfo: IMUser? = IMHelper.userHandler.getLocalUserInfo(v2NIMChatroomMessage.senderId),
    val senderAvatar: String? = userInfo?.avatar,
    val senderName: String? = userInfo?.name,
    val time: String = v2NIMChatroomMessage.createTime.format(),
    val text: String? = v2NIMChatroomMessage.text,
    val senderId: String = v2NIMChatroomMessage.senderId,
    val isSelf: Boolean = v2NIMChatroomMessage.isSelf,
    val receiverId: String? = v2NIMChatroomMessage.serverExtension,
    val receiverName : String? =  IMHelper.userHandler.getLocalUserInfo(receiverId)?.name,
    val createTime: Long = v2NIMChatroomMessage.createTime,
    val body: IMChatroomMessageBody?
)

data class IMChatroomMessageBody(
    val code: Int,
    val data: String
)
