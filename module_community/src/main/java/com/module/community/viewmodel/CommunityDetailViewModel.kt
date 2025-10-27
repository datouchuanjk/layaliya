package com.module.community.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.fromJson
import com.module.basic.api.data.request.*
import com.module.basic.viewmodel.BaseViewModel
import com.module.community.api.data.request.CommunityLikeRequest
import com.module.community.api.data.response.CommunityResponse
import com.module.community.api.service.CommunityApiService
import kotlinx.coroutines.launch

internal class CommunityDetailViewModel(
    private val api: CommunityApiService,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    var data by mutableStateOf(
        savedStateHandle.get<String>("data")?.fromJson<CommunityResponse>()
    )

    /**
     * 点赞
     */
    fun like() {
        val item = data ?: return
        val isLike = item.imPraise == 1
        viewModelScope.launch {
            apiRequest {
                if (isLike) {
                    api.dynamicUnlike(
                        CommunityLikeRequest(
                            zoneId = item.id.toString()
                        )
                    ).checkAndGet()
                } else {
                    api.dynamicLike(
                        CommunityLikeRequest(
                            zoneId = item.id.toString()
                        )
                    ).checkAndGet()
                }
            }.apiResponse(null) {
                data = data?.copy(
                    imPraise = if (isLike) 0 else 1,
                    praiseNum = if (isLike) item.praiseNum - 1 else item.praiseNum + 1
                )
            }
        }
    }

    fun follow() {
        val item = data ?: return
        val isFollow = item.isFollow == 1
        viewModelScope.launch {
            apiRequest {
                if (isFollow) {
                    api.unfollowUser(UidRequest(uid = item.uid.toString())).checkAndGet()
                } else {
                    api.followUser(UidRequest(uid = item.uid.toString())).checkAndGet()
                }
            }.apiResponse {
                data = data?.copy(
                    isFollow = if (isFollow) 0 else 1,
                )
            }
        }
    }

    fun refreshCommentCount() {
        data = data?.copy(
            commentNum = data?.commentNum?.plus(1) ?: 0
        )
    }
}