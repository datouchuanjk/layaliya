package com.module.room.viewmodel

import androidx.lifecycle.viewModelScope
import com.module.basic.viewmodel.BaseViewModel
import com.module.room.api.service.RoomApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class RoomCheckViewModel(
    private val api: RoomApiService
) : BaseViewModel() {


    private val _checkResultfulFlow = MutableSharedFlow<Boolean>()
    val checkResultfulFlow = _checkResultfulFlow.asSharedFlow()

    private fun check() {
        viewModelScope.launch {
            apiRequest {
                api.roomCheck().checkAndGet()
            }.apiResponse(
                catch = { _, b->
                    b()
                    _checkResultfulFlow.emit(false)
                }
            ) {
                _checkResultfulFlow.emit(true)
            }
        }
    }

    init {
        check()
    }
}