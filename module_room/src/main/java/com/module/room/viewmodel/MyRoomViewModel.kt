package com.module.room.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.findIndex
import com.module.basic.api.data.request.PagingRequest
import com.module.basic.api.data.response.*
import com.module.basic.sp.*
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import com.module.room.api.service.RoomApiService
import kotlinx.coroutines.launch

internal class MyRoomViewModel(
    type: Int,
    private val api: RoomApiService
) : BaseViewModel() {

    /**
     * 只有我的房间和我管理的才可以显示 hide按钮
     */
    val isShowSwitch: Boolean by mutableStateOf(type == 0 || type == 1)

    /**
     *  type
     *  0 我的房间
     *  1 我管理的房间
     *  2 我关注的房间
     */
    val pagingData = buildOffsetPaging(viewModelScope) {
        when (type) {
            0 -> {
                AppGlobal.userResponse?.roomInfo.run {
                    if (this != null && it.key == 1) {
                        listOf(this)
                    } else {
                        listOf()
                    }
                }
            }

            1 -> {
                api.myManageRoomList(
                    PagingRequest(
                        page = it.key!!,
                    )
                ).checkAndGet()?.list
            }

            2 -> {
                api.myFollowRoomList(
                    PagingRequest(
                        page = it.key!!,
                    )
                ).checkAndGet()?.list
            }

            else -> emptyList()
        }
    }.pagingData


    fun showOrHideRoom(response: RoomResponse) {
        viewModelScope.launch {
            if (response.isOpen == 1) {
                apiRequest {
                    api.hideRoom().checkAndGet()
                }.apiResponse {
                    pagingData.map {
                        findIndex { it.uid == response.uid }?.let { index ->
                            this[index] = response.copy(isOpen = 0)
                        }
                    }
                }
            } else {
                apiRequest {
                    api.showRoom().checkAndGet()
                }.apiResponse {
                    pagingData.map {
                        findIndex { it.uid == response.uid }?.let { index ->
                            this[index] = response.copy(isOpen = 1)
                        }
                    }
                }
            }
        }
    }
}

