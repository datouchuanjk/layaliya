package com.module.noble.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.filterDigit
import com.helper.develop.util.toast
import com.module.basic.api.data.request.UidRequest
import com.module.basic.api.data.response.SearchUserResponse
import com.module.basic.api.service.BasicApiService
import com.module.basic.viewmodel.BaseViewModel
import com.module.noble.R
import com.module.noble.api.service.NobleApiService
import kotlinx.coroutines.launch

internal class GiveToWhoViewModel(
    private val application: Application,
    private val basicApi: BasicApiService
) : BaseViewModel() {

    fun clear() {
        _searchUid = ""
        _findUserResponse = null
    }

    private var _searchUid by mutableStateOf("")
    val searchUid get() = _searchUid
    fun searchUid(uid: String) {
        _searchUid = uid.filterDigit()
    }

    private var _isSearching by mutableStateOf(false)
    val isSearching get() = _isSearching

    private var _findUserResponse by mutableStateOf<SearchUserResponse?>(null)
    val findUserResponse get() = _findUserResponse
    val showUser get() = findUserResponse != null
    fun findUserByUid() {
        if (_searchUid.isEmpty()) {
            return
        }
        viewModelScope.launch {
            apiRequest {
                basicApi.searchUser(UidRequest(uid = searchUid)).checkAndGet()
            }.apiResponse(
                loading = { it, _ ->
                    _isSearching = it
                }
            ) {
                if (it == null||it.id==null) {
                    application.toast(R.string.nobel_not_find_user)
                    return@apiResponse
                }
                _findUserResponse = it

            }
        }
    }
}