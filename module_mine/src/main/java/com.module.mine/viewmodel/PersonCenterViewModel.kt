package com.module.mine.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.findIndex
import com.helper.im.IMHelper
import com.module.basic.api.data.request.UidRequest
import com.module.basic.sp.AppGlobal
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import com.module.mine.api.data.request.PersonDynamicLikeRequest
import com.module.mine.api.data.request.PersonDynamicRequest
import com.module.mine.api.data.response.PersonDynamicResponse
import com.module.mine.api.data.response.PersonResponse
import com.module.mine.api.service.MineApiService
import kotlinx.coroutines.launch

internal class PersonCenterViewModel(
    private val api: MineApiService,
    private val sp: SharedPreferences,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val _uid = savedStateHandle.get<String>("uid").orEmpty()
    val uid get() = _uid

    private var _personResponse by mutableStateOf<PersonResponse?>(null)
    val personResponse get() = _personResponse

    val isSelf get() = AppGlobal.userResponse?.id == _personResponse?.id

    fun getPersonInfo() {
        viewModelScope.launch {
            apiRequest {
                api.personInfo(UidRequest(_uid)).checkAndGet()
            }.apiResponse {
                _personResponse = it
                IMHelper.userHandler.refreshUserInfos(it?.imAccount)
            }
        }
    }

    init {
        getPersonInfo()
    }

    val pagingData = buildOffsetPaging(viewModelScope) {
        api.personDynamicList(
            PersonDynamicRequest(
                uid = _uid,
                page = it.key!!
            )
        ).checkAndGet()?.list
    }.pagingData

    /**
     * 点赞
     */
    fun like(index: Int, item: PersonDynamicResponse) {
        val isLike = item.imPraise == 1
        viewModelScope.launch {
            apiRequest {
                if (isLike) {
                    api.dynamicUnlike(
                        PersonDynamicLikeRequest(
                            zoneId = item.id.toString()
                        )
                    ).checkAndGet()
                } else {
                    api.dynamicLike(
                        PersonDynamicLikeRequest(
                            zoneId = item.id.toString()
                        )
                    ).checkAndGet()
                }
            }.apiResponse(null) {
                pagingData.handle {
                    set(
                        index,
                        item.copy(
                            imPraise = if (isLike) 0 else 1,
                            praiseNum = if (isLike) item.praiseNum - 1 else item.praiseNum + 1
                        )
                    )
                }
            }
        }
    }

    fun refresh(
        index: Int,
        newItem: PersonDynamicResponse
    ) {
        pagingData.handle {
            set(index, newItem)
        }
    }

    fun updateFollowStatus() {
        val response = _personResponse ?: return
        viewModelScope.launch {
            if (response.isFollow == 1) {
                apiRequest {
                    api.unfollowUser(UidRequest(uid = response.id.toString())).checkAndGet()
                }
            } else {
                apiRequest {
                    api.followUser(UidRequest(uid = response.id.toString())).checkAndGet()
                }
            }.apiResponse {
                _personResponse = response.copy(isFollow = if (response.isFollow == 1) 0 else 1)
            }
        }
    }
}