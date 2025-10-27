package com.module.store.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.module.basic.sp.AppGlobal
import com.module.basic.viewmodel.*
import com.module.store.api.data.request.BuyOrSendRequest
import com.module.store.api.data.response.*
import com.module.store.api.service.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class StoreViewModel(
    private val api: StoreApiService,
) : BaseViewModel() {

    init {
        storeInfo()
    }

    private var _index by mutableIntStateOf(0)
    val index get() = _index
    fun index(index: Int) {
        _index = index
        _selectedList.clear()
        val tabIndex = tabs.getOrNull(index)?.index ?: return
        val response = _storeResponse ?: return
        val list = response.list[tabIndex] ?: emptyList()
        _selectedList.addAll(list)
    }


    private var _storeResponse by mutableStateOf<StoreResponse?>(null)
    val tabs get() = _storeResponse?.tabs ?: emptyList()

    private val _selectedList = mutableStateListOf<StoreResponse.Item>()
    val selectedList get() = _selectedList

    fun selectedListItem(item: StoreResponse.Item) {
        _selectedList.forEachIndexed { index, it ->
            if (it.id == item.id) {
                _selectedList[index] = it.copy(isSelected = true)
            } else {
                _selectedList[index] = it.copy(isSelected = false)
            }
        }
    }

    val selectedListItem get() = _selectedList.find { it.isSelected }

    private fun storeInfo() {
        viewModelScope.launch {
            apiRequest {
                api.storeInfo().checkAndGet()
            }.apiResponse {
                _storeResponse = it
                index(0)
            }
        }
    }

    private val _buyOrSendSuccessfulFlow = MutableSharedFlow<Unit>()
    val buyOrSendSuccessfulFlow = _buyOrSendSuccessfulFlow.asSharedFlow()
    fun buy() {
        val id = selectedListItem?.id?:return
        buy(id.toString())
    }
    fun buy(id: String) {
        viewModelScope.launch {
            apiRequest {
                api.buyOrSend(
                    BuyOrSendRequest(
                        goodsId =id,
                        uid = AppGlobal.userResponse?.id.toString(),
                    )
                ).checkAndGet()
            }.apiResponse {
                _buyOrSendSuccessfulFlow.emit(Unit)
            }
        }
    }
}