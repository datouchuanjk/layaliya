package com.module.chatroom.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.module.basic.viewmodel.BaseViewModel
import com.module.chatroom.api.service.ChatroomApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class ChatroomEnterCheckViewModel(
    private val api: ChatroomApiService,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    val roomId = savedStateHandle.get<String>("roomId").orEmpty()

    private val _checkResultfulFlow = MutableSharedFlow<Boolean>()
    val checkResultfulFlow = _checkResultfulFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            apiRequest {
                api.isMysteriousMan().checkAndGet()?.get("is_mysterious_person")
            }.apiResponse(
                catch = { _,it->
                    it()
                    _checkResultfulFlow.emit(false)
                }
            ) {
                _checkResultfulFlow.emit(it==1)
            }
        }
    }
}