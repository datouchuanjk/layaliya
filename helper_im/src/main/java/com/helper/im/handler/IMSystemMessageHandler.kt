package com.helper.im.handler

import com.helper.im.IMHelper
import com.helper.im.data.IMMessage
import com.helper.im.data.transform
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
import com.netease.nimlib.sdk.v2.utils.V2NIMConversationIdUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

 class IMSystemMessageHandler internal constructor(
    scope: CoroutineScope,
) : Handler<V2NIMMessageService>(scope), V2NIMMessageListener {

    private val _receiveMessagesFlow = MutableSharedFlow<IMMessage>()
    val receiveMessagesFlow = _receiveMessagesFlow.asSharedFlow()
    override fun onReceiveMessages(messages: MutableList<V2NIMMessage>?) {
        messages ?: return
        if (messages.isEmpty()) return
        launch {
            messages.filter {
                it.conversationId !in IMHelper.conversationIds
            }.forEach {
                _receiveMessagesFlow.emit(it.transform())
            }
        }
    }

    override fun onReceiveP2PMessageReadReceipts(readReceipts: MutableList<V2NIMP2PMessageReadReceipt>?) {}
    override fun onReceiveTeamMessageReadReceipts(readReceipts: MutableList<V2NIMTeamMessageReadReceipt>?) {}
    override fun onMessageRevokeNotifications(revokeNotifications: MutableList<V2NIMMessageRevokeNotification>?) {}
    override fun onMessagePinNotification(pinNotification: V2NIMMessagePinNotification?) {}
    override fun onMessageQuickCommentNotification(quickCommentNotification: V2NIMMessageQuickCommentNotification?) {}
    override fun onMessageDeletedNotifications(messageDeletedNotifications: MutableList<V2NIMMessageDeletedNotification>?) {}
    override fun onClearHistoryNotifications(clearHistoryNotifications: MutableList<V2NIMClearHistoryNotification>?) {}
    override fun onSendMessage(message: V2NIMMessage?) {}
    override fun onReceiveMessagesModified(messages: MutableList<V2NIMMessage>?) {}

    init {
        register {
            service.addMessageListener(this)
            return@register {
                service.removeMessageListener(this)
            }
        }
    }

    fun testSendMessageTo(to:String){
        val v2TextMessage = V2NIMMessageCreator.createTextMessage("1")
        NIMClient.getService(V2NIMMessageService::class.java).sendMessage(
            v2TextMessage,
            V2NIMConversationIdUtil.conversationId(
                to,
                V2NIMConversationType.V2NIM_CONVERSATION_TYPE_P2P
            ), null, {
            }, {
            }, {
            })
    }
}