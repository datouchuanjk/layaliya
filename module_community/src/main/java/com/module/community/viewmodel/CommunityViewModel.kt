package com.module.community.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.findIndex
import com.module.basic.api.data.request.*
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import com.module.community.api.data.request.CommunityLikeRequest
import com.module.community.api.data.request.CommunityListRequest
import com.module.community.api.data.response.CommunityResponse
import com.module.community.api.service.CommunityApiService
import kotlinx.coroutines.launch

internal class CommunityViewModel(
    private val api: CommunityApiService
) : BaseViewModel() {

    /**
     * 列表
     */
    val pagingData = buildOffsetPaging(viewModelScope) {
        api.communityList(
            CommunityListRequest(
                type = "1",
                page = it.key!!
            )
        ).checkAndGet()?.list
    }.pagingData

    /**
     * 点赞
     */
    fun like(index: Int, item: CommunityResponse) {
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

    fun follow(item: CommunityResponse) {
        val isFollow = item.isFollow == 1
        viewModelScope.launch {
            apiRequest {
                if (isFollow) {
                    api.unfollowUser(UidRequest(uid = item.uid.toString())).checkAndGet()
                } else {
                    api.followUser(UidRequest(uid = item.uid.toString())).checkAndGet()
                }
            }.apiResponse {
                pagingData.map {
                    if (it.uid == item.uid) {
                        it.copy(
                            isFollow = if (isFollow) 0 else 1,
                        )
                    } else {
                        it
                    }
                }
            }
        }
    }

    fun refresh(
        index: Int,
        oldItem: CommunityResponse,
        newItem: CommunityResponse
    ) {
        if (oldItem.isFollow != newItem.isFollow) {
            pagingData.map {
                if (it.id == newItem.id) {
                    newItem
                } else if (it.uid == newItem.uid) {
                    it.copy(
                        isFollow = newItem.isFollow,
                    )
                } else {
                    it
                }
            }
        } else {
            pagingData.handle {
                set(index, newItem)
            }
        }
    }


}