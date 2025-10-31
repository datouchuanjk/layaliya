package com.helper.im.handler

import android.graphics.BitmapFactory
import android.util.Log
import com.helper.develop.paging.LoadResult
import com.helper.develop.paging.PagingConfig
import com.helper.develop.paging.PagingStart
import com.helper.develop.paging.buildPaging
import com.helper.develop.util.*
import com.helper.im.IMHelper
import com.helper.im.data.IMGiftBody
import com.helper.im.data.IMUser
import com.helper.im.data.transform
import com.helper.im.util.logIM
import com.helper.im.transform
import com.helper.im.util.toTargetId
import com.netease.nimlib.sdk.NIMClient
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
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageQueryDirection
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageSendingState
import com.netease.nimlib.sdk.v2.message.option.V2NIMMessageListOption
import com.netease.nimlib.sdk.v2.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import java.io.File
import kotlin.collections.filter
import kotlin.collections.forEachIndexed
import kotlin.collections.map
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class IMMessageHandler internal constructor(
    scope: CoroutineScope,
    private val conversationId: String,
) : Handler<V2NIMMessageService>(scope), V2NIMMessageListener {


    init {
        register {
            IMHelper.conversationHandler.clearUnreadCountById(conversationId)
            IMHelper.conversationHandler.setCurrentConversation(conversationId)
            service.addMessageListener(this)
            return@register {
                service.removeMessageListener(this)
                IMHelper.conversationHandler.setCurrentConversation(null)
            }
        }
    }

    private val _receiveGiftMessagesFlow = MutableSharedFlow<String>()
    val receiveGiftMessagesFlow = _receiveGiftMessagesFlow.asSharedFlow()

    val userInfo = MutableStateFlow<IMUser?>(null)

    val targetId: String? = conversationId.toTargetId()

    init {
        launch {
            IMHelper.userHandler.userProfileChangedFlow.collect {
                it.forEach {
                    if (it.accountId == targetId) {
                        userInfo.value = it
                    }
                }
            }
        }
        launch {
            userInfo.value = withContext(Dispatchers.IO) {
                IMHelper.userHandler.getLocalUserInfo(targetId)
            }
        }
    }

    /**
     *  添加消息监听
     *
     *  @param messages 收到的消息内容
     */
    override fun onReceiveMessages(messages: MutableList<V2NIMMessage>?) {
        logIM("onReceiveMessages messages->${messages}")
        messages ?: return
        if (messages.isEmpty()) return
        launch {
            IMHelper.conversationHandler.clearUnreadCountById(conversationId)
            pagingData.handle {
                addAll(
                    0,
                    messages.filter { it.conversationId == conversationId }.map { it.transform() })
            }
            messages.forEach {
                handleMessage(it)
            }
        }
    }

    /**
     *  点对点已读回执
     *
     *  @param readReceipts 消息已读回执列表
     */
    override fun onReceiveP2PMessageReadReceipts(readReceipts: MutableList<V2NIMP2PMessageReadReceipt>?) {
        logIM("onReceiveP2PMessageReadReceipts readReceipts->${readReceipts}")
        readReceipts ?: return
        if (readReceipts.isEmpty()) return
        pagingData.handle {
            readReceipts
                .filter { it.conversationId == conversationId }
                .forEach { readReceipt ->
                    this.filter {
                        it.createTime <= readReceipt.timestamp
                    }.forEachIndexed { index, value ->
                        this[index] = value.copy(isRead = true)
                    }
                }
        }

    }

    /**
     *  群已读回执
     *
     *  @param readReceipts 消息已读回执列表
     */
    override fun onReceiveTeamMessageReadReceipts(readReceipts: MutableList<V2NIMTeamMessageReadReceipt>?) {
        logIM("onReceiveTeamMessageReadReceipts readReceipts->${readReceipts}")
    }

    /**
     *  消息撤回回调
     *
     *  @param revokeNotifications 消息撤回通知数据
     */
    override fun onMessageRevokeNotifications(revokeNotifications: MutableList<V2NIMMessageRevokeNotification>?) {
        logIM("onMessageRevokeNotifications revokeNotifications->${revokeNotifications}")
    }

    /**
     *  消息pin状态回调通知
     *
     *  @param pinNotification 消息pin状态变化通知数据
     */
    override fun onMessagePinNotification(pinNotification: V2NIMMessagePinNotification?) {
        logIM("onMessagePinNotification pinNotification->${pinNotification}")
    }

    /**
     *  消息评论状态回调
     *
     *  @param quickCommentNotification 快捷评论通知数据
     */
    override fun onMessageQuickCommentNotification(quickCommentNotification: V2NIMMessageQuickCommentNotification?) {
        logIM("onMessageQuickCommentNotification quickCommentNotification->${quickCommentNotification}")
    }

    /**
     *  消息被删除通知
     *
     *  @param messageDeletedNotifications 被删除的消息列表
     *      只支持点对点消息和高级群消息多端删除同步
     */
    override fun onMessageDeletedNotifications(messageDeletedNotifications: MutableList<V2NIMMessageDeletedNotification>?) {
        logIM("onMessageDeletedNotifications messageDeletedNotifications->${messageDeletedNotifications}")
    }

    /**
     *  清空会话历史消息通知
     *
     *  @param clearHistoryNotifications 被删除的消息列表
     *      只支持点对点消息和高级群消息多端删除同步
     */
    override fun onClearHistoryNotifications(clearHistoryNotifications: MutableList<V2NIMClearHistoryNotification>?) {
        logIM("onClearHistoryNotifications clearHistoryNotifications->${clearHistoryNotifications}")
    }

    /**
     *  本端发送消息状态回调
     *  来源： 发送消息， 插入消息
     *
     *  @param message 发送或插入的消息
     *
     */
    override fun onSendMessage(message: V2NIMMessage?) {
        logIM("onSendMessage message->${message}")
        if (message == null) return
        if (message.conversationId != conversationId) return
        pagingData.handle {
            val index = findIndex { it.messageId == message.messageId }
            if (index == null) {
                add(0, message.transform())
            } else {
                this[index] = message.transform()
            }
        }

        if (message.sendingState == V2NIMMessageSendingState.V2NIM_MESSAGE_SENDING_STATE_SUCCEEDED) {
            handleMessage(message)
        }
    }

    private fun handleMessage(message: V2NIMMessage) {
        launch {
            val imMessage = message.transform()
            val body = imMessage.body
            if (body is IMGiftBody) {
                _receiveGiftMessagesFlow.emit(body.string)
            }
        }
    }

    /**
     *  添加消息更新监听
     *  @param messages 收到更新的消息
     */
    override fun onReceiveMessagesModified(messages: MutableList<V2NIMMessage>?) {
        logIM("onReceiveMessagesModified messages->${messages}")
    }

    fun sendP2PMessageReceipt(message: V2NIMMessage) {
        logIM("sendP2PMessageReceipt message->${message}")
        service.sendP2PMessageReceipt(message, {}, {})
    }


    val pagingData = buildPaging(
        coroutineScope = this,
        pagingStart = PagingStart.DEFAULT,
        initialKey = 0L,
        config = PagingConfig(pageSize = 20)
    ) { loadParams ->
        val result = suspendCancellableCoroutine { continuation ->
            service.getMessageList(
                V2NIMMessageListOption.V2NIMMessageListOptionBuilder.builder(conversationId)
                    .withEndTime(loadParams.key!!)
                    .withLimit(loadParams.pageSize)
                    .withDirection(V2NIMMessageQueryDirection.V2NIM_QUERY_DIRECTION_DESC)
                    .build(), {
                    continuation.resume(it)
                }, {
                    throw it.transform()
                })
        }
        LoadResult(
            if (result.size < loadParams.pageSize) null else result.last().createTime,
            result.map { it.transform() }
        )
    }.pagingData

    init {
        launch {
            IMHelper.userHandler.userProfileChangedFlow.collect { users ->
                pagingData.handle {
                    users.forEach { user ->
                        this.forEachIndexed { index, item ->
                            if (user.accountId == item.senderId) {
                                this[index] =
                                    item.copy(senderAvatar = user.avatar, senderName = user.name)
                            } else if (user.accountId == item.receiverId) {
                                this[index] = item.copy(
                                    receiverAvatar = user.avatar,
                                    receiverName = user.name
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 发送文本消息
     */
    suspend fun sendTextMessage(message: String) {
        if (message.isEmpty()) return
        logIM("sendTextMessage message->${message}")
        val v2TextMessage = V2NIMMessageCreator.createTextMessage(message)
        suspendCancellableCoroutine { b ->
            service.sendMessage(
                v2TextMessage,
                conversationId,
                null, {
                    b.resume(Unit)
                }, {
                    b.resumeWithException(it.transform())
                }, {
                })
        }
    }

    /**
     * 发送图片消息
     */
    suspend fun sendImageMessage(file: File) {
        logIM("sendImageMessage file->${file}")
        suspendCancellableCoroutine { b ->
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(file.absolutePath, options)
            val v2TextMessage = V2NIMMessageCreator.createImageMessage(
                file.absolutePath,
                file.name,
                null,
                options.outWidth,
                options.outHeight
            )
            service.sendMessage(
                v2TextMessage,
                conversationId, null, {
                    b.resume(Unit)
                }, {
                    b.resumeWithException(it.transform())
                }, {
                })
        }
    }

    /**
     * 发送礼物消息
     */
    suspend fun sendGiftMessage(string: String) {
        suspendCancellableCoroutine { b ->
            val jsonObject = JSONObject()
            jsonObject.put("code", 1000)
            jsonObject.put("data", IMGiftBody(string = string).toJson())
            val v2TextMessage = V2NIMMessageCreator.createCustomMessage("", jsonObject.toString())
            service.sendMessage(
                v2TextMessage,
                conversationId, null, {
                    b.resume(Unit)
                }, {
                    b.resumeWithException(it.transform())
                }, {
                })
        }
    }
}