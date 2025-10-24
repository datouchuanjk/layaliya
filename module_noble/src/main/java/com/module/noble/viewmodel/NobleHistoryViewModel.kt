package com.module.noble.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.toast
import com.module.basic.api.data.request.PagingRequest
import com.module.basic.util.*
import com.module.basic.viewmodel.BaseViewModel
import com.module.noble.R
import com.module.noble.api.data.response.CanReturnDiamondResponse
import com.module.noble.api.service.NobleApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class NobleHistoryViewModel(
    private val application: Application,
    private val api: NobleApiService,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    init {
        getCanReceiveDiamond()
    }

    val name  = savedStateHandle.get<String>("name").orEmpty()
    private var _canReturnDiamondResponse by mutableStateOf<CanReturnDiamondResponse?>(null)
    val canReturnDiamondResponse get() = _canReturnDiamondResponse

    val isNoble get() = _canReturnDiamondResponse?.isNoble == 1
    private fun getCanReceiveDiamond() {
        viewModelScope.launch {
            apiRequest {
                api.canReceiveDiamond().checkAndGet()
            }.apiResponse {
                _canReturnDiamondResponse = it
            }
        }
    }

    private val _receiveDiamondFlow = MutableSharedFlow<Unit>()
    val receiveDiamondFlow = _receiveDiamondFlow.asSharedFlow()
    fun receiveDiamond() {
        if (_canReturnDiamondResponse?.isNoble != 1) {
            application.toast(R.string.nobel_receive_failed)
            return
        }
        viewModelScope.launch {
            apiRequest {
                api.receiveDiamond().checkAndGet()
                api.canReceiveDiamond().checkAndGet()
            }.apiResponse {
                _canReturnDiamondResponse = it
                _receiveDiamondFlow.emit(Unit)
            }
        }
    }

    val pagingData = buildOffsetPaging(viewModelScope) {
        api.buyOrGiveNobleList(PagingRequest(it.key!!)).checkAndGet()?.list
    }.pagingData
}