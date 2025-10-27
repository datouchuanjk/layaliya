package com.module.gift.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.*
import com.helper.develop.nav.*
import com.helper.im.IMHelper
import com.helper.im.data.IMUser
import com.module.basic.sp.AppGlobal
import com.module.basic.viewmodel.*
import com.module.gift.api.data.request.SendGiftRequest
import com.module.gift.api.data.response.*
import com.module.gift.api.service.*
import kotlinx.coroutines.*
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
    init {
        val a =""
    }
    private val _yxIds = savedStateHandle.get<String>("yxIds").orEmpty().split(",").toList()

    private val _userInfos = mutableStateMapOf<String, IMUser?>()

    val userInfos get() = _userInfos.filter { it.value != null }.map { it.value }

    private var _currentUserInfo by mutableStateOf<IMUser?>(null)
    val currentUserInfo get() = _currentUserInfo
    fun setCurrentUserInfo(index: Int) {
        _currentUserInfo = userInfos[index]
    }

    init {
        _userInfos.putAll(IMHelper.userHandler.getLocalUserInfos(_yxIds))
        _currentUserInfo = userInfos.getOrNull(0)
        viewModelScope.launch {
            IMHelper.userHandler.userProfileChangedFlow.collect {
                it.forEach { newUser ->
                    if (_userInfos.contains(newUser.accountId)) {
                        _userInfos[newUser.accountId] = newUser
                    }
                }
            }
        }
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
                val jsonObject = JSONObject()
                jsonObject.put("sendUid", AppGlobal.userResponse?.id.toString())
                jsonObject.put("sendName", AppGlobal.userResponse?.nickname.toString())
                jsonObject.put("sendAvatar", AppGlobal.userResponse?.avatar.toString())
                jsonObject.put("receiveName", currentUserInfo?.name)
                jsonObject.put("giftId", giftId.toString())
                jsonObject.put("giftName", giftName)
                jsonObject.put("giftCount", selectedNum)
                jsonObject.put("floatingScreenId", floatingScreenId)
                localNav.emitResult("send_gift_result", jsonObject.toString())
            }
        }
    }
}