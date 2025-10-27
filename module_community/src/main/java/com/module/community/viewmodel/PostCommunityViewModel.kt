package com.module.community.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.module.basic.util.UploadUtils
import com.module.basic.viewmodel.BaseViewModel
import com.module.community.api.data.request.PostCommunityRequest
import com.module.community.api.service.CommunityApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.*

internal class PostCommunityViewModel(
    private val api: CommunityApiService,
    private val helper: UploadUtils,
) : BaseViewModel() {

    private var _input by mutableStateOf("")
    val input get() = _input
    fun input(input: String) {
        _input = input
    }


    private var _images = mutableStateListOf<String>()
    private var _isShowAddImage by mutableStateOf(true)
    val isShowAddImage get() = _isShowAddImage
    val lastImageCount get() = 3 - _images.count()
    val images get() = _images

    fun addImage(image: List<File>) {
        if (_images.count() == 3) return
        viewModelScope.launch {
            flow {
                emit(helper.uploadFiles(UploadUtils.TAG_ZONE, image))
            }.apiResponse {
                _images.addAll(0, it)
                _isShowAddImage = _images.count() < 3
            }
        }
    }

    /**
     * 发布
     */
    private val _postCommunitySuccessfulFlow = MutableSharedFlow<Unit>()
    val postCommunitySuccessfulFlow = _postCommunitySuccessfulFlow.asSharedFlow()
    fun post() {
        if (images.isEmpty() && input.isEmpty()) {
            return
        }
        viewModelScope.launch {
            apiRequest {
                api.poshCommunity(
                    PostCommunityRequest(
                        content = input,
                        images = images
                    )
                )
            }.apiResponse {
                _postCommunitySuccessfulFlow.emit(Unit)
            }
        }
    }
}