package com.module.noble.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.module.basic.sp.AppGlobal
import com.module.basic.viewmodel.BaseViewModel
import com.module.noble.api.data.request.BuyNobleRequest
import com.module.noble.api.data.request.GiveNobleRequest
import com.module.noble.api.data.response.NobleResponse
import com.module.noble.api.service.NobleApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class NobleViewModel(
    private val api: NobleApiService
) : BaseViewModel() {

    init {
        nobleInfo()
    }

    val nobleLevel get() = AppGlobal.userResponse?.nobleLevel ?: 0


    /**
     * 当前选中的
     */
    private var _selectedIndex by mutableIntStateOf(if (nobleLevel > 0) nobleLevel - 1 else 0)
    val  selectedIndex get()= _selectedIndex
    val nobleExpireTime by mutableStateOf(
        if (nobleLevel> 0) AppGlobal.userResponse?.nobleExpireTime else null
    )

    fun selectedIndex(index: Int) {
        _selectedIndex = index
    }

    /**
     * 当前选中的对象
     */
    val selectedNobleResponse get() = _nobleList.getOrNull(_selectedIndex)
    val selectedNobelPowerDataList get() = selectedNobleResponse?.powerData ?: emptyList()

    /**
     * 集合
     */
    private val _nobleList = mutableStateListOf<NobleResponse>()
    val nobleList get() = _nobleList

    private fun nobleInfo() {
        viewModelScope.launch {
            apiRequest {
                api.nobleInfo().checkAndGet()!!
            }.apiResponse {
                _nobleList.clear()
                _nobleList.addAll(it)
            }
        }
    }

    private val _buyFlow = MutableSharedFlow<Unit>()
    val buyFlow = _buyFlow.asSharedFlow()
    fun buy() {
        val level = selectedNobleResponse?.level ?: return
        viewModelScope.launch {
            apiRequest {
                api.buyNoble(
                    BuyNobleRequest(
                        level = level
                    )
                ).checkAndGet()
            }.apiResponse {
                _buyFlow.emit(Unit)
            }
        }
    }

    private val _giveFlow = MutableSharedFlow<Unit>()
    val giveFlow = _giveFlow.asSharedFlow()
    fun give(uid: String) {
        val level = selectedNobleResponse?.level ?: return
        viewModelScope.launch {
            apiRequest {
                api.giveNoble(
                    GiveNobleRequest(
                        level = level,
                        uid = uid
                    )
                ).checkAndGet()
            }.apiResponse {
                _giveFlow.emit(Unit)
            }
        }
    }
}