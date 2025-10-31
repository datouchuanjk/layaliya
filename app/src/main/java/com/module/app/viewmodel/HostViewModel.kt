package com.module.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.helper.im.IMHelper
import com.module.basic.viewmodel.BaseViewModel

import kotlinx.coroutines.launch

class HostViewModel : BaseViewModel() {

    /**
     * 收到全局的飘萍通知
     */
    val top = mutableStateListOf<String?>()
    var top1 by mutableStateOf<String?>(null)
    var top2 by mutableStateOf<String?>(null)
    var top3 by mutableStateOf<String?>(null)


    init {
        viewModelScope.launch {
            IMHelper.notificationMessageHandler.receiveMessagesFlow.collect {
                if (top.size > 50) {
                    return@collect
                }
                top.add(it)
                if (top1 == null) {
                    top1 = get()
                }
                if (top2 == null) {
                    top2 = get()
                }
                if (top3 == null) {
                    top3 = get()
                }
            }
        }
    }

    fun get(): String? {
        val item = top.getOrNull(0)
        top.remove(item)
        return item
    }

    fun test() {
        IMHelper.notificationMessageHandler.test()
    }
}