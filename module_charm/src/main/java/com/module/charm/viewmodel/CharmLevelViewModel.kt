package com.module.charm.viewmodel

import androidx.lifecycle.viewModelScope
import com.module.basic.viewmodel.BaseViewModel
import com.module.charm.api.data.response.CharmLevelResponse
import com.module.charm.api.service.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CharmLevelViewModel(
    private val api: CharmApiService
) : BaseViewModel() {

    init {
        getCharmLevel()
    }

    /**
     * 获取信息
     */
    private val _charmLevelFlow = MutableStateFlow<CharmLevelResponse?>(null)
    val charmLevelFlow = _charmLevelFlow.asStateFlow()
    private fun getCharmLevel() {
        viewModelScope.launch {
            apiRequest {
                api.charmLevel().checkAndGet()!!
            }.apiResponse {
                _charmLevelFlow.emit(it)
            }
        }
    }
}