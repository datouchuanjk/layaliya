package com.module.comment.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.findIndex
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import com.module.comment.api.data.request.CommentLikeRequest
import com.module.comment.api.data.request.CommentListRequest
import com.module.comment.api.data.request.PostCommentRequest
import com.module.comment.api.data.response.CommentResponse
import com.module.comment.api.service.CommentApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class CommentViewModel(
    savedStateHandle: SavedStateHandle,
    private val api: CommentApiService
) : BaseViewModel() {

    private var _businessId: String = savedStateHandle.get<String>("id").orEmpty()

    /**
     * 列表
     */
    var pagingData = buildOffsetPaging(viewModelScope) {
        api.commentList(
                CommentListRequest(
                    zoneId = _businessId,
                    page = it.key!!
                )
            ).checkAndGet()?.list
    }.pagingData

    /**
     * 点赞
     */
    fun like(index: Int, item: CommentResponse) {
        val isLike = item.imPraise == 1
        viewModelScope.launch {
            apiRequest {
                if (isLike) {
                    api.commentUnlike(
                                        CommentLikeRequest(
                                            commentId = item.id.toString()
                                        )
                                    ).checkAndGet()
                } else {
                    api.commentLike(
                                        CommentLikeRequest(
                                            commentId = item.id.toString()
                                        )
                                    ).checkAndGet()
                }
            }.apiResponse(null) {
                pagingData.map {
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

    private var _input by mutableStateOf("")
    val input get() = _input
    fun input(input: String) {
        _input = input
    }

    /**
     * 被回复的对象
     */
    private var _commentResponse by mutableStateOf<CommentResponse?>(null)
    val commentResponse get() = _commentResponse
    fun commentId(response: CommentResponse?) {
        _commentResponse = response
    }

    private var _commentCount = 0
    private val _postCommentSuccessfulFlow = MutableSharedFlow<Int>()
    val postCommentSuccessfulFlow = _postCommentSuccessfulFlow.asSharedFlow()
    private var _isCommenting by mutableStateOf(false)
    val isCommenting get() = _isCommenting
    fun postComment() {
        if (_input.isEmpty()) return
        viewModelScope.launch {
            apiRequest {
                api.postComment(
                                PostCommentRequest(
                                    zoneId = _businessId,
                                    content = _input,
                                    commentId = _commentResponse?.id?.toString() ?: "0"
                                )
                            ).checkAndGet()
            }.apiResponse(
                loading = { it,_->
                    _isCommenting = it
                }
            ) { result ->
                _commentCount++
                _input = ""
                _postCommentSuccessfulFlow.emit(_commentCount)
                result?.let {
                    pagingData.map {
                        if (_commentResponse == null) {
                            add(0, result)
                        } else {
                            findIndex {
                                it.id == _commentResponse!!.id
                            }?.let { findIndex ->
                                if (findIndex == lastIndex) {
                                    add(result)
                                } else {
                                    add(findIndex + 1, result)
                                }
                            } ?: add(0, result)
                        }
                    }
                }

            }
        }
    }
}