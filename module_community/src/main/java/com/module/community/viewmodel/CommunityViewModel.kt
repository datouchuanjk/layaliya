package com.module.community.viewmodel

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

    fun follow(index: Int, item: CommunityResponse) {
        val isFollow = item.isFollow == 1
        viewModelScope.launch {
            apiRequest {
                if (isFollow) {
                    api.unfollowUser(UidRequest(uid = item.uid.toString())).checkAndGet()
                } else {
                    api.followUser(UidRequest(uid = item.uid.toString())).checkAndGet()
                }
            }.apiResponse {
                pagingData.handle {
                    set(
                        index,
                        item.copy(
                            isFollow = if (isFollow) 0 else 1,
                        )
                    )
                }
            }
        }
    }

    fun uploadCommentNum(communityResponse: CommunityResponse, count: Int) {
        pagingData.handle {
            findIndex { it.id == communityResponse.id }?.let { index ->
                this[index] = communityResponse.copy(
                    commentNum = communityResponse.commentNum + count
                )
            }
        }
    }
}