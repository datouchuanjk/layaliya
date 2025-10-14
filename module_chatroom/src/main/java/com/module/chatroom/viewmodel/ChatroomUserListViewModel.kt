package com.module.chatroom.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import com.module.chatroom.api.data.request.ChatroomSearchRequest
import com.module.chatroom.api.data.response.ChatroomUserResponse
import com.module.chatroom.api.service.ChatroomApiService

internal class ChatroomUserListViewModel(
    private val api: ChatroomApiService,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private var _searchUid by mutableStateOf("")
    val searchUid get() = _searchUid
    fun searchUid(uid: String){
        _searchUid = uid
    }

    fun search(){
        userList.refresh()
    }
    /**
     * 房间id
     */
   private val roomId = savedStateHandle.get<String>("roomId").orEmpty()

    val userList = buildOffsetPaging(viewModelScope) {
        api.getRoomUserListByKey(ChatroomSearchRequest(roomId = roomId, key = _searchUid))
          .checkAndGet()?.list
    }.pagingData
}