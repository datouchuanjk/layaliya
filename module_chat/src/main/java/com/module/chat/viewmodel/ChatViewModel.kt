package com.module.chat.viewmodel

import android.app.Application
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.toast
import com.helper.im.IMHelper
import com.helper.im.data.*
import com.module.basic.viewmodel.BaseViewModel
import com.module.chat.R
import com.module.chat.api.data.request.AcceptInviteRequest
import com.module.chat.api.service.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File

internal class ChatViewModel(
    private val api: ChatApiService,
    val application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val conversationId = savedStateHandle.get<String>("conversationId").orEmpty()
    private val messageHandler = IMHelper.messageHandler(
        scope = viewModelScope,
        conversationId = conversationId,
    )

    val receiveGiftMessagesFlow = messageHandler.receiveGiftMessagesFlow

    val pagingData = messageHandler.pagingData
    val lazyState = LazyListState(0, 0)


    val userInfo = messageHandler.userInfo.asStateFlow()

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


    fun handleGift(string: String?) {
        string ?: return
        viewModelScope.launch {
            messageHandler.sendGiftMessage(string)
        }
    }

    /**
     * 同意
     */
    fun acceptInvite(id: String) {
        viewModelScope.launch {
            apiRequest {
                api.acceptInvite(AcceptInviteRequest(id)).checkAndGet()
            }.apiResponse() {
                application.toast(R.string.chat_accept_invite)
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