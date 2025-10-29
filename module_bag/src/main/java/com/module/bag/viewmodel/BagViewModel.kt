package com.module.bag.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.helper.develop.util.toast
import com.module.bag.api.data.request.UseBagRequest
import com.module.basic.viewmodel.*
import com.module.bag.api.data.response.*
import com.module.bag.api.service.BagApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import com.module.bag.R

internal class BagViewModel(
    private val api: BagApiService,
    private val application: Application,
) : BaseViewModel() {

    init {
        bagInfo()
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


    private var _storeResponse by mutableStateOf<BagResponse?>(null)
    val tabs get() = _storeResponse?.tabs ?: emptyList()

    private val _selectedList = mutableStateListOf<BagResponse.Item>()
    val selectedList get() = _selectedList

    fun selectedListItem(item: BagResponse.Item) {
        _selectedList.forEachIndexed { index, it ->
            if (it.id == item.id) {
                _selectedList[index] = it.copy(isSelected = true)
            } else {
                _selectedList[index] = it.copy(isSelected = false)
            }
        }
    }

    val selectedListItem get() = _selectedList.find { it.isSelected }

    private fun bagInfo() {
        viewModelScope.launch {
            apiRequest {
                api.bagInfo().checkAndGet()
            }.apiResponse {
                _storeResponse = it
                index(0)
            }
        }
    }

    private val _useSuccessfulFlow = MutableSharedFlow<Unit>()
    val useSuccessfulFlow = _useSuccessfulFlow.asSharedFlow()
    fun use() {
        val item = selectedListItem ?: return
        use(item.id.toString(), item.use.orEmpty())
    }

    fun use(id: String, use: String) {
        if (use == "1") {
            application.toast(R.string.bag_already_used)
            return
        }
        viewModelScope.launch {
            apiRequest {
                api.use(
                    UseBagRequest(
                        propId = id
                    )
                ).checkAndGet()
            }.apiResponse {
                _useSuccessfulFlow.emit(Unit)
            }
        }
    }
}