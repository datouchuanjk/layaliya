package com.helper.im.handler

import android.util.Log
import com.helper.develop.paging.LoadResult
import com.helper.develop.paging.buildPaging
import com.helper.im.*
import com.helper.im.data.IMChatroomMessage
import com.helper.im.data.IMChatroomMessageBody
import com.helper.im.data.transform
import com.netease.nimlib.sdk.v2.*
import com.netease.nimlib.sdk.v2.auth.*
import com.netease.nimlib.sdk.v2.chatroom.*
import com.netease.nimlib.sdk.v2.chatroom.enums.*
import com.netease.nimlib.sdk.v2.chatroom.model.*
import com.netease.nimlib.sdk.v2.chatroom.params.*
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageSendingState
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.json.JSONObject
import java.lang.Exception
import kotlin.collections.forEachIndexed
import kotlin.coroutines.*


class IMChatroomHandler(
    scope: CoroutineScope,
) : Handler<V2NIMLoginService>(scope), V2NIMChatroomClientListener, V2NIMChatroomListener {

    private var _chatroomClient: V2NIMChatroomClient = V2NIMChatroomClient.newInstance()
    private val _chatroomService = _chatroomClient.chatroomService
    private val _outFlow = MutableSharedFlow<Boolean>()
    val outFlow = _outFlow.asSharedFlow()
    private val _mikeInfoChangeFlow = MutableSharedFlow<String>()
    val mikeInfoChangeFlow = _mikeInfoChangeFlow.asSharedFlow()

    private val _receiveGiftFlow = MutableSharedFlow<String>()
    val receiveGiftFlow = _receiveGiftFlow.asSharedFlow()
    private val _receiveEmojiFlow = MutableSharedFlow<String>()
    val receiveEmojiFlow = _receiveEmojiFlow.asSharedFlow()
    private val _roomInfoChangeFlow = MutableSharedFlow<String>()
    val roomInfoChangeFlow = _roomInfoChangeFlow.asSharedFlow()
    private val _myRoleChangeFlow = MutableSharedFlow<Int?>()
    val myRoleChangeFlow = _myRoleChangeFlow.asSharedFlow()
    private val _userJoinFlow = MutableSharedFlow<String>()
    val userJoinFlow = _userJoinFlow.asSharedFlow()
    private val _myBeenSilenceFlow = MutableSharedFlow<Long>()
    val myBeenSilenceFlow = _myBeenSilenceFlow.asSharedFlow()

    private val _roomUserCountChangeFlow = MutableSharedFlow<Int>()
    val roomUserCountChangeFlow = _roomUserCountChangeFlow.asSharedFlow()

    private val _roomTop5UsersChangeFlow = MutableSharedFlow<String>()
    val roomTop5UsersChangeFlow = _roomTop5UsersChangeFlow.asSharedFlow()

    val allPagingData = buildPaging(this) {
        LoadResult.empty<IMChatroomMessage>()
    }.pagingData

    init {
        launch {
            IMHelper.userHandler.userProfileChangedFlow.collect { users ->
                allPagingData.handle {
                    users.forEach { user ->
                        this.forEachIndexed { index, item ->
                            if (user.accountId == item.senderId) {
                                this[index] = item.copy(
                                    senderAvatar = user.avatar,
                                    senderName = user.name
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        register {
            _chatroomClient.addChatroomClientListener(this)
            _chatroomService?.addChatroomListener(this)
            return@register {
                _chatroomClient.removeChatroomClientListener(this)
                _chatroomService?.removeChatroomListener(this)
            }
        }
    }

    suspend fun enter(
        roomId: String,
        accountId: String,
        token: String,
    ) {
        val address = suspendCancellableCoroutine { cancellableContinuation ->
            service.getChatroomLinkAddress(roomId, {
                cancellableContinuation.resume(it)
            }, {
                Log.e("1234", "获取聊天室地址 $it")
                cancellableContinuation.resumeWithException(it.transform())
            })
        }
        val params = V2NIMChatroomEnterParams
            .V2NIMChatroomEnterParamsBuilder.builder { roomId, accountId ->
                address
            }.withAccountId(accountId)
            .withToken(token)
            .build()
        suspendCancellableCoroutine { cancellableContinuation ->
            _chatroomClient.enter(roomId, params, {
                Log.e("1234", "我进入了聊天室")
                cancellableContinuation.resume(Unit)
            }, {
                Log.e("1234", "进入聊天室异常 $it")
                cancellableContinuation.resumeWithException(it.transform())
            })
        }
    }

    fun exit() {
        _chatroomClient.exit()
        V2NIMChatroomClient.destroyInstance(_chatroomClient.instanceId)
        launch {
            _outFlow.emit(false)
        }
    }

    suspend fun sendEmoji(string: String) {
        val jsonObject = JSONObject().apply {
            put("code", 1036)
            put("data", JSONObject().apply {
                put("emoji", JSONObject(string))
            })
        }
        suspendCancellableCoroutine { b ->
            val message = V2NIMChatroomMessageCreator.createCustomMessage(jsonObject.toString())
            _chatroomService?.sendMessage(message, null, {
                b.resume(Unit)
            }, {
                b.resumeWithException(it.transform())
            }, {})
        }
    }

    suspend fun sendGift(string: String) {
        val jsonObject = JSONObject().apply {
            put("code", 1037)
            put("data", JSONObject().apply {
                put("gift", JSONObject(string))
            })
        }
        suspendCancellableCoroutine { b ->
            val message = V2NIMChatroomMessageCreator.createCustomMessage(jsonObject.toString())
            _chatroomService?.sendMessage(message, null, {
                b.resume(Unit)
            }, {
                b.resumeWithException(it.transform())
            }, {})
        }
    }

    suspend fun kickout(accountId: String) {
        suspendCancellableCoroutine { b ->
            _chatroomService?.kickMember(accountId, null, {
                b.resume(Unit)
            }, {
                b.resumeWithException(it.transform())
            })
        }
    }


    suspend fun sendTextMessage(text: String, toAccId: String? = null) {
        if(text.isEmpty())return
        suspendCancellableCoroutine { b ->
            val message = V2NIMChatroomMessageCreator.createTextMessage(text)
            val params = if (toAccId.isNullOrEmpty()) {
                null
            } else {
                V2NIMSendChatroomMessageParams().apply {
                    receiverIds = listOf(toAccId)
                }
            }
            message.serverExtension = toAccId
            _chatroomService?.sendMessage(message, params, {
                b.resume(Unit)
            }, {
                b.resumeWithException(it.transform())
            }, {})
        }
    }

    suspend fun sendJoinCurrentMessage(text: String) {
        val jsonObject = JSONObject().apply {
            put("code", 1003)
            put("data", JSONObject().apply {
                put("notice", JSONObject(text))
            })
        }
        suspendCancellableCoroutine { b ->
            val message = V2NIMChatroomMessageCreator.createCustomMessage(jsonObject.toString())
            _chatroomService?.sendMessage(message, null, {
                b.resume(Unit)
            }, {
                b.resumeWithException(it.transform())
            }, {})
        }
    }

    override fun onChatroomKicked(kickedInfo: V2NIMChatroomKickedInfo?) {
        launch {
            _outFlow.emit(true)
        }
    }

    override fun onReceiveMessages(messages: List<V2NIMChatroomMessage>?) {
        messages?.filterNotNull()?.forEach { handleMessage(it) }
    }


    override fun onSendMessage(message: V2NIMChatroomMessage?) {
        message?.let { handleMessage(it) }
    }


    private fun handleMessage(message: V2NIMChatroomMessage) {
        if (message.isSelf && message.sendingState != V2NIMMessageSendingState.V2NIM_MESSAGE_SENDING_STATE_SUCCEEDED) {
            return
        }
        if (message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_CUSTOM) {
            handleCurrentMessage(message)
        } else if (message.messageType == V2NIMMessageType.V2NIM_MESSAGE_TYPE_TEXT) {
            handleTextMessage(message)
        }
    }

    private fun handleCurrentMessage(message: V2NIMChatroomMessage) {
        Log.e("1234", "message.attachment.raw=${message.attachment.raw}")
        val json = try {
            JSONObject(message.attachment.raw)
        } catch (_: Exception) {
            return
        }
        val code = try {
            json.getInt("code")
        } catch (_: Exception) {
            return
        }
        val data = try {
            json.getJSONObject("data")
        } catch (_: Exception) {
            return
        }
        Log.e("1234", "code=$code data=${data}")
        when (code) {
            1002 -> {
                //麦序变化
                launch {
                    _mikeInfoChangeFlow.emit(data.getString("mike_info"))
                }
            }
            1036 -> {
                //收到礼物
                launch {
                    Log.e("1234","我收到了表情 自定义消息收到 发送给房间")
                    _receiveEmojiFlow.emit(data.getString("emoji"))
                }
            }
            1037 -> {
                //收到礼物
                launch {
                    Log.e("1234","我收到了礼物 自定义消息收到 发送给房间")
                    val gift = data.getString("gift")
                    _receiveGiftFlow.emit(gift)
                    allPagingData.handle {
                        add(0, message.transform(IMChatroomMessageBody(code = code, data = gift)))
                    }
                }
            }

            1005 -> {
                //房间信息变化
                launch {
                    _roomInfoChangeFlow.emit(data.getString("room_info"))
                }
                launch {
                    _mikeInfoChangeFlow.emit(data.getString("mike_info"))
                }
            }

            1004 -> {
                //用户身份变化
                launch {
                    _myRoleChangeFlow.emit(data.getInt("role"))
                }
            }

            1003 -> {
                //有人进入
                launch {
                    val notice = data.getString("notice")
                    _userJoinFlow.emit(notice)
                    //同时也加入消息体
                    allPagingData.handle {
                        add(0, message.transform(IMChatroomMessageBody(code = code, data = notice)))
                    }
                }
            }

            1014 -> {
                //被禁言
                launch {
                    _myBeenSilenceFlow.emit(data.getLong("last_time"))
                }
            }

            1015 -> {
                //房间人数发生变化
                launch {
                    _roomTop5UsersChangeFlow.emit(data.getString("top5"))
                }
                launch {
                    _roomUserCountChangeFlow.emit(data.getInt("user_num"))
                }
            }
        }
    }

    private fun handleTextMessage(message: V2NIMChatroomMessage) {
        allPagingData.handle {
            add(0, message.transform())
        }
    }


    override fun onChatroomStatus(status: V2NIMChatroomStatus?, error: V2NIMError?) {}
    override fun onChatroomEntered() {}
    override fun onChatroomExited(error: V2NIMError?) {}
    override fun onChatroomMemberEnter(member: V2NIMChatroomMember?) {}
    override fun onChatroomMemberExit(accountId: String?) {}

    override fun onChatroomMemberRoleUpdated(
        previousRole: V2NIMChatroomMemberRole?,
        member: V2NIMChatroomMember?
    ) {
    }

    override fun onChatroomMemberInfoUpdated(member: V2NIMChatroomMember?) {
    }

    override fun onSelfChatBannedUpdated(chatBanned: Boolean) {
    }

    override fun onSelfTempChatBannedUpdated(
        tempChatBanned: Boolean,
        tempChatBannedDuration: Long
    ) {
    }

    override fun onChatroomInfoUpdated(chatroom: V2NIMChatroomInfo?) {
    }

    override fun onChatroomChatBannedUpdated(chatBanned: Boolean) {
    }

    override fun onMessageRevokedNotification(
        messageClientId: String?,
        messageTime: Long
    ) {
    }

    override fun onChatroomTagsUpdated(tags: List<String?>?) {
    }

}