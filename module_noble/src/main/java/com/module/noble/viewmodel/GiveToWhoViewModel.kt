package com.module.noble.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.module.basic.viewmodel.BaseViewModel
import com.module.noble.api.data.request.FindUserRequest
import com.module.noble.api.data.response.FindUserResponse
import com.module.noble.api.service.NobleApiService
import kotlinx.coroutines.launch

internal class GiveToWhoViewModel(private val api: NobleApiService) : BaseViewModel() {

    private var _searchUid by mutableStateOf("")
    val searchUid get() = _searchUid
    fun searchUid(uid: String) {
        _searchUid = uid
    }

    private var _isSearching by mutableStateOf(false)
    val isSearching get() = _isSearching

    private var _findUserResponse by mutableStateOf<FindUserResponse?>(null)
    val findUserResponse get() = _findUserResponse
    val showUser get() = findUserResponse != null
    fun findUserByUid() {
        if (_searchUid.isEmpty()) {
            return
        }
        viewModelScope.launch {
            apiRequest {
                api.findUser(FindUserRequest(uid = searchUid)).checkAndGet()!!
            }.apiResponse(
                loading = {it,_->
                    _isSearching = it
                }
            ) {
                _findUserResponse = it
            }
        }
    }
}