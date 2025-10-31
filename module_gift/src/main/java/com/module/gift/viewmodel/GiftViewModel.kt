package com.module.gift.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.*
import com.helper.develop.nav.*
import com.helper.develop.util.buildJsonObject
import com.helper.develop.util.fromJson
import com.module.basic.sp.AppGlobal
import com.module.basic.viewmodel.*
import com.module.gift.api.data.GiftInfo
import com.module.gift.api.data.request.SendGiftRequest
import com.module.gift.api.data.response.*
import com.module.gift.api.service.*
import kotlinx.coroutines.*

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
        _selectedItem = null
    }


    private var _giftResponse by mutableStateOf<GiftResponse?>(null)
    val tabs get() = _giftResponse?.tabs ?: emptyList()

    private val _selectedList = mutableStateListOf<GiftResponse.Item>()
    val selectedList get() = _selectedList

    private var _selectedItem by mutableStateOf<GiftResponse.Item?>(null)
     val selectedItemHasSvg
        get() = if (_selectedItem?.id == null) false else AppGlobal.getGiftSvgFileById(
            _selectedItem?.id.toString()
        ).run {
            this.exists() && this.length() > 0
        }
    val selectedItem get() = _selectedItem
    fun selectedListItem(item: GiftResponse.Item) {
        _selectedItem = item
        _numIndex =0
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

    val numList get() =  listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    private var _numIndex by mutableIntStateOf(0)
    fun numIndex(index: Int) {
        _numIndex = index
    }

    val selectedNum get() = numList[_numIndex]
    private var _sendGiftLoading by mutableStateOf(false)
    val sendGiftLoading get() = _sendGiftLoading

    private val _giftInfo = savedStateHandle.get<String>("json")?.fromJson<GiftInfo>()

    val userInfos get() = _giftInfo?.userInfo.orEmpty()

    private val _roomId get() = _giftInfo?.roomId ?: "0"


    private var _currentUserInfo by mutableStateOf(
        userInfos.getOrNull(
            0
        )
    )
    val currentUserInfo get() = _currentUserInfo
    fun setCurrentUserInfo(index: Int) {
        _currentUserInfo = userInfos[index]
    }


    fun sendGift(localNav: NavHostController) {
        val giftId = selectedListItem?.id ?: return
        val giftName = selectedListItem?.name ?: return
        val floatingScreenId = selectedListItem?.floatingScreenId ?: return
        viewModelScope.launch {
            apiRequest {
                api.sendGift(
                    SendGiftRequest(
                        roomId = _roomId,
                        receiveUid = _currentUserInfo?.uid.orEmpty(),
                        num = selectedNum,
                        giftId = giftId
                    )
                ).checkAndGet()
            }.apiResponse(loading = { it, _ ->
                _sendGiftLoading = it
            }) {
                val json = buildJsonObject { js ->
                    js.put("sendUid", AppGlobal.userResponse?.id.toString())
                    js.put("sendName", AppGlobal.userResponse?.nickname.toString())
                    js.put("sendAvatar", AppGlobal.userResponse?.avatar.toString())
                    js.put("receiveName", currentUserInfo?.nickname.toString())
                    js.put("receiveAvatar", currentUserInfo?.avatar.toString())
                    js.put("giftId", giftId.toString())
                    js.put("giftName", giftName)
                    js.put("giftCount", selectedNum)
                    js.put("floatingScreenId", floatingScreenId)
                }.toString()
                localNav.emitResultTo(key="send_gift_result", value = json)
                localNav.popBackStack()
            }
        }
    }
}