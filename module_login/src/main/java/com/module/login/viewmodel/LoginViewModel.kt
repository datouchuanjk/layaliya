package com.module.login.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.module.basic.api.service.BasicApiService
import com.module.basic.sp.putToken
import com.module.basic.viewmodel.BaseViewModel
import com.module.login.api.data.request.GoogleLoginRequest
import com.module.login.api.service.LoginApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class LoginViewModel(
    private val sp: SharedPreferences,
    private val api: LoginApiService,
    private val basicApi: BasicApiService,
) : BaseViewModel() {

    /**
     * 是否选中隐私政策
     */
    private var _isCheck by mutableStateOf(true)
    val isCheck get() = _isCheck
    fun check() {
        _isCheck = !_isCheck
    }

    private val _loginWithNoCheck = MutableSharedFlow<Unit>()
    val loginWithNoCheck = _loginWithNoCheck.asSharedFlow()

    /**
     * 登录
     */
    private val _loginResultFlow = MutableSharedFlow<Boolean>()
    val loginResultFlow = _loginResultFlow.asSharedFlow()
    fun googleLogin(id: String) {
        viewModelScope.launch {
            if (!_isCheck) {
                _loginWithNoCheck.emit(Unit)
                return@launch
            }
            apiRequest {
                api.googleLogin(GoogleLoginRequest(id)).checkAndGet()?.token?.let {
                    sp.putToken(it)
                } ?: throw NullPointerException()
                check(basicApi)
            }.apiResponse {
                _loginResultFlow.emit(it)
            }
        }
    }

    fun facebookLogin() {
        testLogin()
    }

    /**
     * 登录完成之后
     */
    private fun testLogin() {
        viewModelScope.launch {
            if (!_isCheck) {
                _loginWithNoCheck.emit(Unit)
                return@launch
            }
            apiRequest {
                api.login().checkAndGet()?.token?.let {
                    sp.putToken(it)
                } ?: throw NullPointerException()
                check(basicApi)
            }.apiResponse {
                _loginResultFlow.emit(it)
            }
        }
    }
}