package com.module.agent.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.filterDigit
import com.module.agent.api.data.request.CoinMerchantSendRequest
import com.module.agent.api.service.AgentApiService
import com.module.basic.api.data.request.UidRequest
import com.module.basic.api.data.response.SearchUserResponse
import com.module.basic.api.data.response.UserResponse
import com.module.basic.api.service.BasicApiService
import com.module.basic.sp.AppGlobal
import com.module.basic.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class CoinMerchantViewModel(
    private val api: AgentApiService,
    private val basicApi: BasicApiService,
) : BaseViewModel() {

    val userResponse get() = AppGlobal.userResponse

    /**
     * 输入的uid
     */
    private var _uid by mutableStateOf("")
    val uid get() = _uid
    fun uid(uid: String) {
        _uid = uid.filterDigit()
    }


    private var _userInfo by mutableStateOf<SearchUserResponse?>(null)
    val userInfo get() = _userInfo

    fun query() {
        if (_uid.isEmpty()) {
            return
        }
        viewModelScope.launch {
            apiRequest {
                basicApi.searchUser(
                                UidRequest(_uid)
                            ).checkAndGet()
            }.apiResponse {
                _userInfo = it
            }
        }
    }

    /**
     * 输入的num
     */
    private var _num by mutableStateOf("")
    val num get() = _num
    fun num(num: String) {
        _num = num.filterDigit()
    }

    private val _confirmSuccessful = MutableSharedFlow<Unit>()
    val confirmSuccessful = _confirmSuccessful.asSharedFlow()
    fun confirm() {
        val uid = _userInfo?.id ?: return
        val num = _num.toIntOrNull() ?: return
        viewModelScope.launch {
            apiRequest {
                api.sendDiamondWithCoinMerchant(
                    CoinMerchantSendRequest(
                        uid = uid.toString(),
                        num = num,
                    )
                            ).checkAndGet()
            }.apiResponse()
        }
    }
}