package com.module.room.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.*
import com.module.basic.api.data.response.ConfigResponse
import com.module.basic.sp.AppGlobal
import com.module.basic.util.UploadUtils
import com.module.basic.viewmodel.BaseViewModel
import com.module.room.R
import com.module.room.api.data.request.RoomCreateOrEditRequest
import com.module.room.api.data.request.RoomInfo
import com.module.room.api.service.RoomApiService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

internal class CreateOrEditRoomViewModel(
    private val uploadUtils: UploadUtils,
    private val api: RoomApiService,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    val isEdit = savedStateHandle.get<Boolean>("isEdit") ?: false

    /**
     * 获取修改的房间信息
     */
    private val _roomInfo = savedStateHandle.get<String>("roomInfo")?.fromJson<RoomInfo>()

    /**
     * 名字
     */
    private var _name by mutableStateOf(_roomInfo?.name.orEmpty())
    val name get() = _name
    fun name(name: String) {
        _name = name
    }

    /**
     * 描述
     */
    private var _announcement by mutableStateOf(_roomInfo?.introduce.orEmpty())
    val announcement get() = _announcement
    fun announcement(name: String) {
        _announcement = name
    }

    /**
     * 国家
     */
    private var _country by mutableStateOf(AppGlobal.getCountryByCode(_roomInfo?.countryCode))
    val country get() = _country
    fun country(value: ConfigResponse.Country) {
        _country = value
    }

    /**
     * 房间类型
     */
    val roomTypeList = AppGlobal.configResponse?.roomType ?: emptyList()
    private var _roomType by mutableStateOf(AppGlobal.getRoomTypeById(_roomInfo?.type))
    val roomType get() = _roomType
    fun roomType(value: ConfigResponse.RoomType) {
        _roomType = value
    }

    /**
     * 语言类型
     */
    private var _language by mutableStateOf(AppGlobal.getLanguageByCode(_roomInfo?.language))
    val language get() = _language
    fun language(value: ConfigResponse.Language) {
        _language = value
    }

    data class MicNum(
        val image: Int,
        val textColor: Color,
        val name: String,
        val code: Int,
        val count: Int,
    )

    /**
     * num
     */
    val numList = listOf(
        MicNum(R.drawable.room_ic_mic_10, Color.Black, "10", 1, 10),
        MicNum(R.drawable.room_ic_mic_12, Color.Black, "12", 2, 12),
        MicNum(R.drawable.room_ic_mic_15, Color.Black, "15", 3, 15),
        MicNum(R.drawable.room_ic_mic_16, Color(0xffE49A17), "16-VIP", 4, 16),
        MicNum(R.drawable.room_ic_mic_20, Color(0xffE49A17), "20-VIP", 5, 20),
        MicNum(R.drawable.room_ic_mic_13, Color(0xffE49A17), "Stage-VIP", 6, 13)
    )
    private var _micNum by mutableStateOf<MicNum?>(numList.find { it.count == _roomInfo?.mikeNum })
    val micNum get() = _micNum
    fun micNum(micNum: MicNum) {
        _micNum = micNum
    }

    private var _cover by mutableStateOf(_roomInfo?.cover)
    val cover get() = _cover
    fun cover(file: File) {
        viewModelScope.launch {
            flow {
                emit(uploadUtils.uploadFile(UploadUtils.TAG_ROOM, file))
            }.apiResponse {
                _cover = it
            }
        }
    }

    private val _roomCreateSuccessful = MutableSharedFlow<String>()
    val roomCreateSuccessful = _roomCreateSuccessful.asSharedFlow()

    private val _roomEditSuccessful = MutableSharedFlow<Unit>()
    val roomEditSuccessful = _roomEditSuccessful.asSharedFlow()
    fun roomCreate() {
        viewModelScope.launch {
            apiRequest {
                val type = api.roomCheck().checkAndGet()!!.type
                if (_roomInfo == null) {
                    val result = api.roomCreate(
                        RoomCreateOrEditRequest(
                            init = type,
                            cover = _cover.orEmpty(),
                            name = name,
                            introduce = announcement,
                            countryCode = country?.code.orEmpty(),
                            language = language?.code.orEmpty(),
                            type = roomType?.id?.toString() ?: "1",
                            mode = micNum?.code.toString()
                        )
                    ).checkAndGet()
                    val roomId = JSONObject(result?.get("room_info")!!).getIntOrNull("id").toString()
                    roomId?.let {
                        _roomCreateSuccessful.emit(it)
                    }
                } else {
                    api.roomEdit(
                        RoomCreateOrEditRequest(
                            init = type,
                            cover = _cover.orEmpty(),
                            name = name,
                            introduce = announcement,
                            countryCode = country?.code.orEmpty(),
                            language = language?.code.orEmpty(),
                            type = roomType?.id?.toString() ?: "1",
                            mode = micNum?.code.toString()
                        )
                    ).checkAndGet()
                    _roomEditSuccessful.emit(Unit)
                }
            }.apiResponse()
        }
    }
}