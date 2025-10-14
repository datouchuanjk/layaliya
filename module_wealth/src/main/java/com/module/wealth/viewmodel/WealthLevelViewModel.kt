package com.module.wealth.viewmodel

import androidx.lifecycle.viewModelScope
import com.module.basic.viewmodel.BaseViewModel
import com.module.wealth.api.data.response.WealthLevelResponse
import com.module.wealth.api.service.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class WealthLevelViewModel(
    private val api: WealthApiService
) : BaseViewModel() {

    init {
        getWealthLevel()
    }

    /**
     * 获取信息
     */
    private val _wealthLevelFlow = MutableStateFlow<WealthLevelResponse?>(null)
    val wealthLevelFlow = _wealthLevelFlow.asStateFlow()
    private fun getWealthLevel() {
        viewModelScope.launch {
            apiRequest {
                api.wealthLevel().checkAndGet()!!
            }.apiResponse {
                _wealthLevelFlow.emit(it)
            }
        }
    }
}