package com.module.app.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.module.basic.api.data.request.TokenRequest
import com.module.basic.api.service.BasicApiService
import com.module.basic.route.AppRoutes
import com.module.basic.sp.getToken
import com.module.basic.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LauncherViewModel(
    private val sp: SharedPreferences,
    private val basicApi: BasicApiService,
) : BaseViewModel() {

    private val _routeFlow = MutableSharedFlow<String>()
    val routeFlow = _routeFlow.asSharedFlow()
    fun check() {
        viewModelScope.launch {
            val token = sp.getToken()
            if (token.isEmpty()) {
                _routeFlow.emit(AppRoutes.Login.static)
            } else {
                apiRequest {
                    basicApi.autoLogin(TokenRequest(sp.getToken()))
                    if (check(basicApi)) {
                        AppRoutes.Main.static
                    } else {
                        AppRoutes.PersonEdit.dynamic(
                            "fromComplete" to true
                        )
                    }
                }.apiResponse {
                    _routeFlow.emit(it)
                }
            }
        }
    }
}