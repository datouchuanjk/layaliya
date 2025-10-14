package com.module.gift.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.*
import com.helper.develop.nav.*
import com.helper.develop.util.*
import com.helper.im.IMHelper
import com.helper.im.util.toConversationId
import com.module.basic.sp.AppGlobal
import com.module.basic.viewmodel.*
import com.module.gift.api.data.*
import com.module.gift.api.data.request.SendGiftRequest
import com.module.gift.api.data.response.*
import com.module.gift.api.service.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONObject

internal class GiftViewModel(
    savedStateHandle: SavedStateHandle,
    private val api: GiftApiService
) : BaseViewModel() {


    private var _index by mutableIntStateOf(0)
    val index get() = _index
    fun index(index: Int) {
        _index = index
        _selectedList.clear()
        _numIndex = 0
        val tabIndex = tabs.getOrNull(index)?.index ?: return
        val response = _giftResponse ?: return
        val list = response.list[tabIndex] ?: emptyList()
        _selectedList.addAll(list)
    }


    private var _giftResponse by mutableStateOf<GiftResponse?>(null)
    val tabs get() = _giftResponse?.tabs ?: emptyList()

    private val _selectedList = mutableStateListOf<GiftResponse.Item>()
    val selectedList get() = _selectedList

    fun selectedListItem(item: GiftResponse.Item) {
        _selectedList.forEachIndexed { index, it ->
            if (it.id == item.id) {
                _selectedList[index] = it.copy(isSelected = true)
            } else {
                _selectedList[index] = it.copy(isSelected = false)
            }
        }
    }

    private val selectedListItem get() = _selectedList.find { it.isSelected }

    private var _getGiftInfoLoading by mutableStateOf(false)
    val getGiftInfoLoading get() = _getGiftInfoLoading
    private fun getGiftInfo() {
        viewModelScope.launch {
            apiRequest {
                api.getGiftInfo().checkAndGet()
            }.apiResponse(loading = { it, _ ->
                _getGiftInfoLoading = it
            }) {
                _giftResponse = it
                index(0)
            }
        }
    }

    init {
        getGiftInfo()
    }

    val numList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    private var _numIndex by mutableIntStateOf(0)
    fun numIndex(index: Int) {
        _numIndex = index
    }

    val selectedNum get() = numList[_numIndex]
    private var _sendGiftLoading by mutableStateOf(false)
    val sendGiftLoading get() = _sendGiftLoading
    private val _roomId = savedStateHandle.get<String>("roomId").orEmpty()
    private val _receiveUid = savedStateHandle.get<String>("receiveUid").orEmpty()
    private val _receiveAvatar = savedStateHandle.get<String>("receiveAvatar").orEmpty()
    val receiveName = savedStateHandle.get<String>("receiveName").orEmpty()


    fun sendGift(localNav: NavHostController) {
        val giftId = selectedListItem?.id ?: return
        val giftName = selectedListItem?.name ?: return
        val floatingScreenId = selectedListItem?.floatingScreenId ?: return
        viewModelScope.launch {
            apiRequest {
                api.sendGift(
                    SendGiftRequest(
                        roomId = _roomId,
                        receiveUid = _receiveUid,
                        num = selectedNum,
                        giftId = giftId
                    )
                ).checkAndGet()
            }.apiResponse(loading = { it, _ ->
                _sendGiftLoading = it
            }) {
            val jsonObject = JSONObject()
            jsonObject.put("sendUid", AppGlobal.userResponse?.id.toString())
            jsonObject.put("sendName", AppGlobal.userResponse?.nickname.toString())
            jsonObject.put("sendAvatar", AppGlobal.userResponse?.avatar.toString())
            jsonObject.put("receiveUid", _receiveUid)
            jsonObject.put("receiveName", receiveName)
            jsonObject.put("receiveAvatar", _receiveAvatar)
            jsonObject.put("giftId", giftId.toString())
            jsonObject.put("giftName", giftName)
            jsonObject.put("giftCount", selectedNum)
            jsonObject.put("floatingScreenId", floatingScreenId)
            localNav.emitResult("send_gift_result", jsonObject.toString())
            }
        }
    }
}