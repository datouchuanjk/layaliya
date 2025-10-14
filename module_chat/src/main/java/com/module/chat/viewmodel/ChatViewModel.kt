package com.module.chat.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.im.IMHelper
import com.helper.im.data.*
import com.module.basic.viewmodel.BaseViewModel
import com.module.chat.api.data.request.AcceptInviteRequest
import com.module.chat.api.service.*
import com.netease.nimlib.sdk.v2.message.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File

internal class ChatViewModel(
    private val api: ChatApiService,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val conversationId = savedStateHandle.get<String>("conversationId").orEmpty()
    private val messageHandler = IMHelper.messageHandler(
        scope = viewModelScope,
        conversationId = conversationId,
    )


    val pagingData = messageHandler.pagingData
    val lazyState = LazyListState(0, 0)
    val receiveMessagesFlow = messageHandler.receiveMessagesFlow.map {
        lazyState.firstVisibleItemIndex == 0
    }

    val userInfo = messageHandler.userInfo.asStateFlow()
    val targetId: String? get() = messageHandler.targetId

    /**
     * 输入的文本
     */
    private var _input by mutableStateOf("")
    val input get() = _input
    fun input(input: String) {
        _input = input
    }

    /**
     * 发送文本消息
     */
    fun sendText() {
        if (_input.isEmpty()) {
            return
        }
        viewModelScope.launch {
            apiRequest {
                messageHandler.sendTextMessage(_input.trim())
            }.apiResponse(loading = null) {
                _input = ""
            }
        }
    }

    /**
     * 发送图片消息
     */
    fun sendImage(file: File) {
        viewModelScope.launch {
            apiRequest {
                messageHandler.sendImageMessage(file)
            }.apiResponse(loading = null) {
                _input = ""
            }
        }
    }


    fun handleGift(string: String) {
        viewModelScope.launch {
            messageHandler.sendGiftMessage(string)
        }
    }

    /**
     * 同意
     */
    fun acceptInvite(message: IMMessage, body: IMInvitationBody,id:String) {
        viewModelScope.launch {
            apiRequest {
                api.acceptInvite(AcceptInviteRequest(id))
            }.apiResponse {

            }
        }
    }

    fun sendP2PMessageReceipt(message: IMMessage) {
        messageHandler.sendP2PMessageReceipt(message.v2NIMMessage)
    }

    override fun onCleared() {
        super.onCleared()
        messageHandler.cancel()
    }
}