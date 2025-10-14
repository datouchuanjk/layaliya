package com.module.room.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.module.basic.api.data.request.PagingRequest
import com.module.basic.api.data.response.*
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import com.module.room.api.data.request.FollowRoomRequest
import com.module.room.api.service.RoomApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class RoomViewModel(
    private val type: Int,
    private val api: RoomApiService
) : BaseViewModel() {

    /**
     * 这一块是关注的
     */
    private val _userListFlow = MutableStateFlow<List<UserResponse>>(emptyList())
    val userListFlow = _userListFlow.asStateFlow()
    private var _followType: String = ""

     val isShowUserList by mutableStateOf(type == 0)
    val followPagingDate = buildOffsetPaging(viewModelScope) {
        if (type == 0) {
            if (it.key == 1) {
                val result = api.followRoom().checkAndGet()!!
                _userListFlow.emit(result.users ?: emptyList())
                _followType = result.roomListType.orEmpty()
                result.rooms.list
            } else {
                api.followRoomPage(
                    FollowRoomRequest(
                        type = _followType,
                        page = it.key!!
                    )
                ).checkAndGet()?.list
            }
        } else {
            api.recommendRoom(
                PagingRequest(
                    page = it.key!!
                )
            ).checkAndGet()?.list
        }
    }.pagingData
}

