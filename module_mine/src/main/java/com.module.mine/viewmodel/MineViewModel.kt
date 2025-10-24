package com.module.mine.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.module.basic.api.service.BasicApiService
import com.module.basic.sp.AppGlobal
import com.module.basic.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

internal class MineViewModel(
    private val basicApiService: BasicApiService
) : BaseViewModel() {

    val userInfo get() = AppGlobal.userResponse

    private var _isRefresh by mutableStateOf(false)
    val isRefresh get() = _isRefresh
    fun refresh() {
        viewModelScope.launch {
            apiRequest {
                basicApiService.user().checkAndGet()
            }.apiResponse(
                loading = {loading,block->
                    _isRefresh = loading
                }
            ) {
                AppGlobal.userResponse(it)
            }
        }
    }
    }