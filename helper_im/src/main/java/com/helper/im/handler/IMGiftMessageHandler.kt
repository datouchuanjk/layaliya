package com.helper.im.handler

import com.helper.im.data.*
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.v2.conversation.enums.V2NIMConversationType
import com.netease.nimlib.sdk.v2.message.V2NIMClearHistoryNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.netease.nimlib.sdk.v2.message.V2NIMMessageCreator
import com.netease.nimlib.sdk.v2.message.V2NIMMessageDeletedNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageListener
import com.netease.nimlib.sdk.v2.message.V2NIMMessagePinNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageQuickCommentNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageRevokeNotification
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService
import com.netease.nimlib.sdk.v2.message.V2NIMP2PMessageReadReceipt
import com.netease.nimlib.sdk.v2.message.V2NIMTeamMessageReadReceipt
import com.netease.nimlib.sdk.v2.message.enums.*
import com.netease.nimlib.sdk.v2.utils.V2NIMConversationIdUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.json.JSONObject

class IMGiftMessageHandler internal constructor(
    scope: CoroutineScope,
) : Handler<V2NIMMessageService>(scope), V2NIMMessageListener {

    private val _receiveMessagesFlow = MutableSharedFlow<String>()
    val receiveMessagesFlow = _receiveMessagesFlow.asSharedFlow()
    override fun onReceiveMessages(messages: MutableList<V2NIMMessage>?) {
        messages ?: return
        if (messages.isEmpty()) return
        messages.forEach {
            handle(it)
        }
    }

    override fun onReceiveP2PMessageReadReceipts(readReceipts: MutableList<V2NIMP2PMessageReadReceipt>?) {}
    override fun onReceiveTeamMessageReadReceipts(readReceipts: MutableList<V2NIMTeamMessageReadReceipt>?) {}
    override fun onMessageRevokeNotifications(revokeNotifications: MutableList<V2NIMMessageRevokeNotification>?) {}
    override fun onMessagePinNotification(pinNotification: V2NIMMessagePinNotification?) {}
    override fun onMessageQuickCommentNotification(quickCommentNotification: V2NIMMessageQuickCommentNotification?) {}
    override fun onMessageDeletedNotifications(messageDeletedNotifications: MutableList<V2NIMMessageDeletedNotification>?) {}
    override fun onClearHistoryNotifications(clearHistoryNotifications: MutableList<V2NIMClearHistoryNotification>?) {}
    override fun onSendMessage(message: V2NIMMessage?) {
        message ?: return
        if (message.sendingState == V2NIMMessageSendingState.V2NIM_MESSAGE_SENDING_STATE_SUCCEEDED) {
            handle(message)
        }
    }

    override fun onReceiveMessagesModified(messages: MutableList<V2NIMMessage>?) {}

    private fun handle(message: V2NIMMessage) {
        launch {
            val imMessage = message.transform()
            val body = imMessage.body
            if (body is IMGiftBody) {
                _receiveMessagesFlow.emit(body.string)
            }
        }
    }

    init {
        register {
            service.addMessageListener(this)
            return@register {
                service.removeMessageListener(this)
            }
        }
    }

}