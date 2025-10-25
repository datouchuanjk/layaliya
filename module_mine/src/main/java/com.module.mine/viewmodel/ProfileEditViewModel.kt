package com.module.mine.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.YMD
import com.helper.develop.util.year
import com.helper.im.IMHelper
import com.helper.im.handler.IMUserHandler
import com.module.basic.api.service.BasicApiService
import com.module.basic.sp.AppGlobal
import com.module.basic.util.UploadUtils
import com.module.basic.viewmodel.BaseViewModel
import com.module.mine.api.data.request.*
import com.module.mine.api.data.request.CompleteUserInfoRequest
import com.module.mine.api.data.request.EditUserInfoRequest
import com.module.mine.api.service.MineApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.*
import java.util.Calendar

internal class ProfileEditViewModel(
    private val api: MineApiService,
    private val basicApi: BasicApiService,
    private val uploadUtils: UploadUtils,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    //是否是通过 完善资料进来的，
    val isFomComplete = savedStateHandle.get<Boolean>("fromComplete") ?: false

    private val userInfo = AppGlobal.userResponse
    private var _avatar by mutableStateOf(userInfo?.avatar)
    val avatar get() = _avatar
    fun avatar(file: File) {
        viewModelScope.launch {
            flow {
                emit(uploadUtils.uploadFile(UploadUtils.TAG_AVATAR, file))
            }.apiResponse {
                if (it.isNotEmpty()) {
                    _avatar = it
                }
            }
        }
    }

    private var _introduce by mutableStateOf(userInfo?.nickname)
    val introduce get() = _introduce
    fun introduce(introduce: String) {
        _introduce = introduce
    }

    private var _nickname by mutableStateOf(userInfo?.nickname)
    val nickname get() = _nickname
    fun nickname(nickname: String) {
        _nickname = nickname
    }

    private var _sex by mutableStateOf(userInfo?.sex)
    val sex get() = _sex
    fun sex(sex: Int) {
        _sex = sex
    }


    private var _language by mutableStateOf(userInfo?.language)
    val language get() = _language
    fun language(value: String) {
        _language = value
    }


    private var _countryCode by mutableStateOf(userInfo?.language)
    val countryCode get() = _countryCode
    fun countryCode(value: String) {
        _countryCode = value
    }

    private var _birthDay by mutableStateOf(userInfo?.birthDay)
    val birthDay get() = _birthDay
    fun birthDay(birthDay: String) {
        _birthDay = birthDay
    }

    init {
        if (isFomComplete) {
            if (_language .isNullOrEmpty()) {
                _language = AppGlobal.configResponse?.language?.get(0)?.code
            }
            if (_countryCode .isNullOrEmpty()) {
                _countryCode = AppGlobal.configResponse?.country?.get(0)?.code
            }
            if (_birthDay.isNullOrEmpty()) {
                _birthDay = Calendar.getInstance().apply {
                    year =year- 25
                }.YMD
            }
        }
    }

    private val _editSuccessfulFlow = MutableSharedFlow<Unit>()
    val editSuccessfulFlow = _editSuccessfulFlow.asSharedFlow()

    private val _checkCompleteFlow = MutableSharedFlow<Unit>()
    val checkCompleteFlow = _checkCompleteFlow.asSharedFlow()
    fun edit() {
        viewModelScope.launch {
            if (isFomComplete) {
                apiRequest {
                    api.completeUserInfo(
                        CompleteUserInfoRequest(
                            avatar = avatar,
                            nickname = nickname,
                            sex = sex,
                            language = language,
                            countryCode = countryCode,
                            birthDay = birthDay,
                        )
                    ).checkAndGet()
                    check(basicApi, true)
                }.apiResponse {
                    _checkCompleteFlow.emit(Unit)
                }
            } else {
                apiRequest {
                    api.editUserInfo(
                        EditUserInfoRequest(
                            avatar = avatar,
                            nickname = nickname,
                            sex = sex,
                            introduce = introduce,
                            countryCode = countryCode,
                            birthDay = birthDay,
                        )
                    ).checkAndGet()
                    basicApi.user().checkAndGet().let {
                        AppGlobal.userResponse(it)
                    }
                }.apiResponse {
                    _editSuccessfulFlow.emit(Unit)
                }
            }
        }
    }
}