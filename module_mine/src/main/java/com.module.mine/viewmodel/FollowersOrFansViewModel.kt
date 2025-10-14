package com.module.mine.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.findIndex
import com.module.basic.api.data.request.UidRequest
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import com.module.mine.api.data.request.FollowOrFansRequest
import com.module.mine.api.data.response.FollowOrFansResponse
import com.module.mine.api.service.MineApiService
import kotlinx.coroutines.launch

internal class FollowersOrFansViewModel(
    savedStateHandle: SavedStateHandle,
    private val api: MineApiService
) : BaseViewModel() {

    /**
     *  0 关注列表
     *  1 粉丝列表
     */
    private val _type = savedStateHandle.get<Int>("type")
    private val _uid = savedStateHandle.get<String>("uid").orEmpty()
    val type get() = _type

    val pagingDate = buildOffsetPaging(viewModelScope) {
        (if (_type == 0) {
            api.followList(
                FollowOrFansRequest(
                    uid = _uid,
                    page = it.key!!
                )
            )
        } else {
            api.fansList(
                FollowOrFansRequest(
                    uid = _uid,
                    page = it.key!!
                )
            )
        }).checkAndGet()?.list
    }.pagingData


    fun updateStatus(response: FollowOrFansResponse) {
        viewModelScope.launch {
            if (response.status == 1 || response.status == 2) {
                apiRequest {
                    api.unfollowUser(UidRequest(uid = response.uid.toString())).checkAndGet()
                }.apiResponse {
                    pagingDate.map {
                        findIndex { it.uid == response.uid }?.let { index ->
                            if (response.status == 1) {
                                removeAt(index)
                            } else {
                                this[index] = response.copy(status = 0)
                            }
                        }
                    }
                }
            } else {
                apiRequest {
                    api.followUser(UidRequest(uid = response.uid.toString())).checkAndGet()
                }.apiResponse {
                    pagingDate.map {
                        findIndex { it.uid == response.uid }?.let { index ->
                            this[index] = response.copy(status = 2)
                        }
                    }
                }
            }
        }
    }
}