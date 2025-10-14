package com.module.chatroom.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.module.basic.api.data.response.ConfigResponse
import com.module.basic.sp.AppGlobal
import com.module.basic.util.UploadUtils
import com.module.basic.viewmodel.BaseViewModel
import com.module.chatroom.api.data.request.ChatroomReportRequest
import com.module.chatroom.api.service.ChatroomApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File

internal class ChatroomReportViewModel(
    private val api: ChatroomApiService,
    private val helper: UploadUtils,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private var _selectedReport by mutableStateOf<ConfigResponse.ReportType?>(null)
    val selectedReport get() = _selectedReport
    fun selectedReport(value: ConfigResponse.ReportType) {
        _selectedReport = value
    }

    val reportList = if (savedStateHandle.get<Int>("type") == 0) {
        AppGlobal.configResponse?.reportType?.userReportTypes?.filterNotNull()
    } else {
        AppGlobal.configResponse?.reportType?.roomReportTypes?.filterNotNull()
    } ?: emptyList()

    val objId = savedStateHandle.get<String>("objId").orEmpty()

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
     * 举报提交
     */
    private val _postSuccessfulFlow = MutableSharedFlow<Unit>()
    val postSuccessfulFlow = _postSuccessfulFlow.asSharedFlow()
    fun post() {
        if (input.isEmpty()) {
            return
        }
        if (images.isEmpty()) {
            return
        }
        if (selectedReport==null) {
            return
        }
        viewModelScope.launch {
            apiRequest {
                api.reportRoom(
                    ChatroomReportRequest(
                        reportObjId = objId,
                        content = input,
                        reportTypeId = selectedReport?.id.toString()
                    )
                )
            }.apiResponse {
                _postSuccessfulFlow.emit(Unit)
            }
        }
    }
}