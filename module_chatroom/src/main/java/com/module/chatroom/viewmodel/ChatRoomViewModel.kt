package com.module.chatroom.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.core.content.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.develop.paging.PagingStart
import com.helper.develop.util.fromJson
import com.helper.develop.util.fromTypeJson
import com.helper.develop.util.getStringOrNull
import com.helper.develop.util.toJson
import com.helper.develop.util.toast
import com.helper.im.IMHelper
import com.module.basic.api.data.request.UidRequest
import com.module.basic.sp.AppGlobal
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import com.module.chatroom.R
import com.module.chatroom.api.data.request.ChatroomEnterRequest
import com.module.chatroom.api.data.request.ChatroomInfoRequest
import com.module.chatroom.api.data.request.ChatroomListRequest
import com.module.chatroom.api.data.request.ChatroomMutedRequest
import com.module.chatroom.api.data.request.ChatroomSeatRequest
import com.module.chatroom.api.data.request.ChatroomUserRequest
import com.module.chatroom.api.data.request.ChatroomUserSeatRequest
import com.module.chatroom.api.data.response.ChatroomInfoResponse
import com.module.chatroom.api.data.response.ChatroomUserDetailResponse
import com.module.chatroom.api.data.response.ChatroomUserResponse
import com.module.chatroom.api.service.ChatroomApiService
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.NullPointerException

internal class ChatRoomViewModel(
    private val application: Application,
    private val api: ChatroomApiService,
    private val sp: SharedPreferences,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    fun handleEmoji(emojiId: String) {
        viewModelScope.launch {
            chatroomHandler.sendEmoji(emojiId)
        }
    }

    fun handleGift(string: String) {
        viewModelScope.launch {
            chatroomHandler.sendGift(string)
        }
    }


    init {
        viewModelScope.launch {
            AppGlobal.exitFlow.collect {
                exitRoom()
            }
        }
    }

    /**
     * 房间id
     */
    val roomId = savedStateHandle.get<String>("roomId").orEmpty()

    //是否可以用神秘人进去
    private val isMysteriousPerson = savedStateHandle.get<Boolean>("isMysteriousPerson") ?: false


    private var senderIsMysteriousPerson = isMysteriousPerson

    private val chatroomHandler = IMHelper.chatroomHandler(viewModelScope)
    private val rtcHandler = IMHelper.rtcHandler(application, viewModelScope)

    val pagingData get() = chatroomHandler.allPagingData

    private val _outFlow = MutableSharedFlow<Unit>()
    val outFlow = _outFlow.asSharedFlow()

    override fun onCleared() {
        super.onCleared()
        chatroomHandler.cancel()
        rtcHandler.cancel()
    }

    /**
     * 输入的文本
     */
    private var _input by mutableStateOf("")
    val input get() = _input
    fun input(input: String) {
        _input = input
    }

    val focusRequester = FocusRequester()

    /**
     * 发送给谁呢？
     */
    private var _toNickname by mutableStateOf("")
    private var _toAccId by mutableStateOf("")
    private var _toIsMysteriousPerson by mutableStateOf(false)
    val toNickname get() = _toNickname
    val receiverIsMysteriousPerson get() = _toIsMysteriousPerson
    fun toNickname() {
        _toNickname = currentUserDetail?.nickname.orEmpty()
        _toIsMysteriousPerson = currentUserDetail?.isMysteriousPerson == 1
        _toAccId = currentUserDetail?.yunxinAccid.orEmpty()
        focusRequester.requestFocus()
    }

    fun clearToNickname() {
        _toNickname = ""
        _toAccId = ""
        _toIsMysteriousPerson = false
    }

    /**
     * 发送文本消息
     */
    fun sendTextMessage() {
        if (_myIsSilence) {
            application.toast(R.string.room_banned_message)
            return
        }
        viewModelScope.launch {
            apiRequest {
                chatroomHandler.sendTextMessage(
                    senderIsMysteriousPerson = senderIsMysteriousPerson,
                    receiverIsMysteriousPerson = receiverIsMysteriousPerson,
                    input, _toAccId
                )
            }.apiResponse(loading = null) {
                _input = ""
                clearToNickname()
            }
        }
    }

    /**
     * 调整礼物的
     */
    val realMikeInfo get() = _chatroomInfoResponse?.mikeInfo?.filter { it?.uid != null && it.uid != 0 && it.uid != AppGlobal.userResponse?.id }

    /**
     *  获取房间信息
     */
    private var _chatroomInfoResponse by mutableStateOf<ChatroomInfoResponse?>(null)
    val chatroomInfoResponse get() = _chatroomInfoResponse

    /**
     * 获取房间的默认的前五个用户作为显示
     */
    private val _chatroomUserInfos = mutableStateListOf<ChatroomUserResponse>()
    val chatroomUserInfos get() = _chatroomUserInfos
    private var _silenceJob: Job? = null

    private val _initFailedFlow = MutableSharedFlow<Unit>()
    val initFailedFlow = _initFailedFlow.asSharedFlow()

    init {
        initRoom()
    }

    val receiveGiftFlow = chatroomHandler.receiveGiftFlow

    private var _isRefreshing by mutableStateOf(false)
    val isRoomRefreshing get() = _isRefreshing
    fun initRoom(isRefresh: Boolean = false) {
        viewModelScope.launch {
            apiRequest {
                _chatroomInfoResponse =
                    api.getRoomInfo(ChatroomEnterRequest(roomId, if (isMysteriousPerson) 1 else 0))
                        .checkAndGet()!!
                _chatroomInfoResponse?.userInfo?.let {
                    handleSilence(if (it.isMuted == 1) it.mutedLastTime?.toLong() ?: 0L else 0L)
                }
                senderIsMysteriousPerson = _chatroomInfoResponse?.userInfo?.isMysteriousPerson == 1
                val yunxinRoomId = _chatroomInfoResponse?.roomInfo?.yunxinRoomId
                    ?: throw NullPointerException("roomId is empty")
                chatroomHandler.enter(
                    roomId = yunxinRoomId,
                    accountId = AppGlobal.userResponse?.imAccount.orEmpty(),
                    token = AppGlobal.userResponse?.imToken.orEmpty(),
                )
                initChatroomFlow()
                val key = "${AppGlobal.userResponse?.id}_lastJoinTime_${roomId}" //包含用户id和房间id 防止串号
                val lastJoinTimeMillis = sp.getLong(key, 0L)
                val currentTimeMillis = System.currentTimeMillis()
                if (currentTimeMillis - lastJoinTimeMillis > 5 * 60 * 1000&&!isRefresh) {
                    chatroomHandler.sendJoinCurrentMessage(
                        AppGlobal.userResponse?.imAccount,
                        _chatroomInfoResponse?.notice?.toJson().orEmpty()
                    )
                    sp.edit {
                        putLong(key, currentTimeMillis)
                    }
                }
                val rtcToken = _chatroomInfoResponse?.roomInfo?.rtcToken
                    ?: throw NullPointerException("token is empty")
                val channelName = _chatroomInfoResponse?.roomInfo?.rtcChannelName
                    ?: throw NullPointerException("channelName is empty")
                rtcHandler.enter(
                    token = rtcToken,
                    channelName = channelName,
                    uid = AppGlobal.userResponse?.id?.toLong() ?: System.currentTimeMillis()
                )

                initRTCFlow()
                _chatroomUserInfos.clear()
                _chatroomUserInfos.addAll(
                    api.getRoomUserList(ChatroomInfoRequest(roomId)).checkAndGet()?.list?.take(5)
                        .orEmpty()
                )
            }.apiResponse(catch = { a, b ->
                _initFailedFlow.emit(Unit)
            }, loading = { a, b ->
                if (isRefresh) {
                    _isRefreshing = a
                } else {
                    b()
                }
            })
        }
    }

    private fun handleSilence(lastTime: Long) {
        if (lastTime <= 0) {
            if (lastTime == -1L) {
                _myIsSilence = true
            } else {
                _myIsSilence = false
            }
            _silenceJob?.cancel()
            _silenceJob = null
            return
        }
        var time = lastTime
        _silenceJob?.cancel()
        _silenceJob = viewModelScope.launch {
            _myIsSilence = true
            while (time > 1) {
                delay(1000)
                time -= 1
            }
            _myIsSilence = false
        }
    }

    /**
     * 监听变化
     */
    private fun initChatroomFlow() {
        viewModelScope.launch {
            chatroomHandler.outFlow.collect {
                if (it) {
                    rtcHandler.exit()
                    application.toast(R.string.room_kicked_from_room)
                }
                _outFlow.emit(Unit)
            }
        }
        viewModelScope.launch {
            chatroomHandler.mikeInfoChangeFlow.collect {
                it.fromTypeJson<List<ChatroomInfoResponse.MikeInfo>>()?.let { newMikeInfos ->
                    val oldMikeInfo = _chatroomInfoResponse?.mikeInfo
                    val result = newMikeInfos.mapIndexed { index, value ->
                        val oldEmojiId = oldMikeInfo?.get(index)?.emojiId
                        value.copy(emojiId = oldEmojiId)
                    }
                    _chatroomInfoResponse = _chatroomInfoResponse?.copy(mikeInfo = result)
                    refreshRTCOpenOrClose()
                }
            }
        }
        viewModelScope.launch {
            chatroomHandler.roomInfoChangeFlow.collect {
                it.fromJson<ChatroomInfoResponse.RoomInfo>()?.let { roomInfo ->
                    _chatroomInfoResponse = _chatroomInfoResponse?.copy(
                        roomInfo = chatroomInfoResponse?.roomInfo?.copy(name = roomInfo.name)
                    )
                }
            }
        }
        viewModelScope.launch {
            chatroomHandler.userJoinFlow.collect {
                it.fromJson<ChatroomInfoResponse.Notice>()?.let { notice ->
                    Toast.makeText(application, notice.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModelScope.launch {
            chatroomHandler.myBeenSilenceFlow.collect {
                if (it == 0L) {
                    application.toast(R.string.room_you_have_been_unmuted)
                } else {
                    application.toast(R.string.room_you_have_been_muted)
                }
                handleSilence(it)
            }
        }
        viewModelScope.launch {
            chatroomHandler.myRoleChangeFlow.collect {
                _chatroomInfoResponse = _chatroomInfoResponse?.copy(
                    userInfo = _chatroomInfoResponse?.userInfo?.copy(role = it)
                )
                if (_chatroomInfoResponse?.userInfo?.isAdmin == true) {
                    application.toast(R.string.room_set_as_admin)
                } else {
                    application.toast(R.string.room_removed_from_admin)
                }
            }
        }
        viewModelScope.launch {
            chatroomHandler.roomUserCountChangeFlow.collect {
                _chatroomInfoResponse = _chatroomInfoResponse?.copy(
                    roomInfo = _chatroomInfoResponse?.roomInfo?.copy(userNum = it)
                )
            }
        }
        viewModelScope.launch {
            chatroomHandler.roomTop5UsersChangeFlow.collect {
                it.fromTypeJson<List<ChatroomUserResponse>>()?.let { users ->
                    chatroomUserInfos.clear()
                    chatroomUserInfos.addAll(users.take(5))
                }
            }
        }

        viewModelScope.launch {
            chatroomHandler.receiveEmojiFlow.collect { it ->
                Log.e("1234", "房间收到了表情自定义消息")
                val jsonObject = JSONObject(it)
                val emojiId = jsonObject.getStringOrNull("emojiId")
                val uid = jsonObject.getString("uid")
                _chatroomInfoResponse?.mikeInfo?.withIndex()
                    ?.find { it.value?.uid.toString() == uid }?.let { indexValue ->
                        val index = indexValue.index
                        val value = indexValue.value
                        val newMikeInfo = _chatroomInfoResponse?.mikeInfo?.toMutableList()
                        newMikeInfo?.set(index, value?.copy(emojiId = emojiId))
                        _chatroomInfoResponse = _chatroomInfoResponse?.copy(mikeInfo = newMikeInfo)
                    }
            }
        }
    }


    /**
     * 正在说话的那些人
     */
    private var _audioStartUids = mutableStateListOf<Long>()
    val audioStartUids get() = _audioStartUids
    private fun initRTCFlow() {
        refreshRTCOpenOrClose()
        viewModelScope.launch {
            rtcHandler.joinFailedFlow.collect {
                _initFailedFlow.emit(Unit)
            }
        }
        viewModelScope.launch {
            rtcHandler.userAudioStart.collect {
                if (!_audioStartUids.contains(it)) {
                    _audioStartUids.add(it)
                }
            }
        }
        viewModelScope.launch {
            rtcHandler.userAudioStop.collect {
                if (_audioStartUids.contains(it)) {
                    _audioStartUids.remove(it)
                }
            }
        }
    }

    private fun refreshRTCOpenOrClose() {
        if (myMikeInfo?.status == 0) {
            rtcHandler.open()
        } else {
            rtcHandler.close()
        }
    }

    /**
     * 房间禁言列表
     */
    val muteList = buildOffsetPaging(viewModelScope, pagingStart = PagingStart.LAZY) {
        api.getRoomMuteList(ChatroomListRequest(roomId = roomId, page = it.key!!))
            .checkAndGet()?.list
    }.pagingData


    /**
     * 房间管理列表
     */
    val adminList = buildOffsetPaging(viewModelScope, pagingStart = PagingStart.LAZY) {
        api.getRoomAdminList(ChatroomInfoRequest(roomId)).checkAndGet()?.list
    }.pagingData


    /**
     * 可踢出列表
     */
    val kickoutList = buildOffsetPaging(viewModelScope, pagingStart = PagingStart.LAZY) {
        api.getRoomUserList(ChatroomInfoRequest(roomId)).checkAndGet()?.list
    }.pagingData


    /**
     * 我的麦信息 如果是null 代表我没有上麦
     */
    val myMikeInfo get() = _chatroomInfoResponse?.mikeInfo?.find { it?.uid == AppGlobal.userResponse?.id }

    /**
     * 我的麦坐标
     */
    private var _myMikeInfoOffset by mutableStateOf<Rect?>(null)
    val myMikeInfoOffset get() = if (myMikeInfo == null) null else _myMikeInfoOffset

    fun setMyMikeInfoOffset(id: Int?, offset: Offset, size: Float) {
        if (id == AppGlobal.userResponse?.id) {
            _myMikeInfoOffset = Rect(offset = offset, size = Size(size, size))
        }
    }


    /**
     * 当前弹框这个人的麦信息 如果是null 代表他没有上麦
     */

    val currentUserMikeInfo get() = _chatroomInfoResponse?.mikeInfo?.find { it?.uid.toString() == _currentUserDetail?.id }

    /**
     * 我是否开麦了
     */
    val myIsOpenMike get() = myMikeInfo?.status == 0

    /**
     * 我是否被禁言
     */
    private var _myIsSilence by mutableStateOf(false)

    /**
     * 找到第一个空麦对象
     */
    val firstEmptyMikeInfo get() = _chatroomInfoResponse?.mikeInfo?.find { it == null || it.uid == 0 }

    /**
     * 获取用户详情
     */
    private var _currentUserDetail by mutableStateOf<ChatroomUserDetailResponse?>(null)
    val currentUserDetail get() = _currentUserDetail

    /**
     * 刷新麦信息
     */
    private suspend fun refreshMikeInfos() {
        //现在不需要单独调用此接口了，全靠推送
        return
//        val result = api.getRoomMikeInfo(ChatroomInfoRequest(roomId)).handle()!!
//        _chatroomInfoResponse = _chatroomInfoResponse?.copy(mikeInfo = result)
    }

    /**
     * 刷新当前用户信息
     */
    private suspend fun refreshCurrentUserInfo() {
        _currentUserDetail ?: return
        val result = api.getMikeUserInfo(
            ChatroomUserRequest(
                uid = _currentUserDetail?.id.toString(),
                roomId = roomId
            )
        ).checkAndGet()!!
        _currentUserDetail = result
    }

    /**
     * 上麦
     */
    fun upSeat(seatId: String) {
        viewModelScope.launch {
            apiRequest {
                api.upSeat(
                    ChatroomSeatRequest(seatId = seatId, roomId = roomId)
                ).checkAndGet()
                senderIsMysteriousPerson = false
                refreshMikeInfos()
            }.apiResponse()
        }
    }

    /**
     * 下麦  下麦之后 肯定是需要刷新麦列表的
     */
    fun downSeat(seatId: String) {
        viewModelScope.launch {
            apiRequest {
                api.downSeat(
                    ChatroomSeatRequest(seatId = seatId, roomId = roomId)
                ).checkAndGet()
                refreshMikeInfos()
            }.apiResponse()
        }
    }

    /**
     * 我自己快速上下麦
     */
    fun quickUpOrDownSeat() {
        val myMikeInfo = myMikeInfo
        if (myMikeInfo == null) {
            //上麦 找到第一个空麦
            val firstEmptyMikeInfo = firstEmptyMikeInfo
            if (firstEmptyMikeInfo == null) {
                application.toast(R.string.room_not_empty_seat)
                return
            }
            upSeat(firstEmptyMikeInfo.id.toString())
        } else {
            //下麦
            downSeat(myMikeInfo.id.toString())
        }
    }


    /**
     *我自己开麦
     */
    fun openSeat() {
        val myMikeInfo = myMikeInfo
        if (myMikeInfo == null) {
            //我没上麦，就不存在开麦
            application.toast(R.string.room_must_up_seat_before_you_open_or_close)
            return
        }
        viewModelScope.launch {
            apiRequest {
                api.openSeat(
                    ChatroomSeatRequest(seatId = myMikeInfo.id.toString(), roomId = roomId)
                ).checkAndGet()
                refreshMikeInfos()
            }.apiResponse()
        }
    }

    /**
     * 我自己关麦
     */
    fun closeSeat() {
        val myMikeInfo = myMikeInfo
        if (myMikeInfo == null) {
            //我没上麦，就不存在关麦
            application.toast(R.string.room_must_up_seat_before_you_open_or_close)
            return
        }
        viewModelScope.launch {
            apiRequest {
                api.closeSeat(
                    ChatroomSeatRequest(seatId = myMikeInfo.id.toString(), roomId = roomId)
                ).checkAndGet()
                refreshMikeInfos()
            }.apiResponse()
        }
    }

    /**
     * 作为管理，关别人的麦
     */
    private val _closeCurrentUserSeatFlow = MutableSharedFlow<Unit>()
    val closeCurrentUserSeatFlow = _closeCurrentUserSeatFlow.asSharedFlow()
    fun closeCurrentUserSeat() {
        Log.e("1234", "closeCurrentUserSeat= ${currentUserMikeInfo}")
        val currentUserMikeInfo = currentUserMikeInfo
        if (currentUserMikeInfo == null) {
            return
        }
        viewModelScope.launch {
            apiRequest {
                api.closeSeat(
                    ChatroomSeatRequest(seatId = currentUserMikeInfo.id.toString(), roomId = roomId)
                ).checkAndGet()
            }.apiResponse {
                _closeCurrentUserSeatFlow.emit(Unit)
            }
        }
    }

    /**
     * 获取当前查看用户的信息，每次都把上一个人清理，避免数据错乱
     */
    fun clearCurrentUserDetail() {
        _currentUserDetail = null
    }

    fun getCurrentUserDetail(uid: String) {
        _currentUserDetail = null
        viewModelScope.launch {
            apiRequest {
                api.getMikeUserInfo(
                    ChatroomUserRequest(
                        uid = uid,
                        roomId = roomId
                    )
                ).checkAndGet()!!

            }.apiResponse {
                _currentUserDetail = it
            }
        }
    }

    /**
     * 取消管理
     * 默认取消的是当前用户
     */
    fun unAdmin(uid: String = currentUserDetail?.id.toString()) {
        viewModelScope.launch {
            apiRequest {
                api.unAdmin(ChatroomUserRequest(uid = uid, roomId = roomId))
                    .checkAndGet()!!
                refreshCurrentUserInfo()
                adminList.handle { removeIf { it.uid.toString() == uid } }
            }.apiResponse()
        }
    }

    /**
     * 添加管理
     */
    fun addAdmin() {
        val uid = currentUserDetail?.id.toString()
        viewModelScope.launch {
            apiRequest {
                api.setManager(ChatroomUserRequest(uid = uid, roomId = roomId)).checkAndGet()!!
                refreshCurrentUserInfo()
            }.apiResponse()
        }
    }

    //踢人
    // 默认踢掉当前用户
    /**
     * 推出
     */
    private val _kickoutUserFlow = MutableSharedFlow<Unit>()
    val kickoutUserFlow get() = _kickoutUserFlow
    fun kickout(
        uid: String = currentUserDetail?.id.toString(),
        imId: String = currentUserDetail?.yunxinAccid.toString()
    ) {
        if (uid.isEmpty()) {
            return
        }
        if (imId.isEmpty()) {
            return
        }
        viewModelScope.launch {
            apiRequest {
                api.kickout(
                    ChatroomUserRequest(uid = uid, roomId = roomId)
                ).checkAndGet()
                refreshMikeInfos()
                chatroomHandler.kickout(imId)
            }.apiResponse {
                kickoutList.handle { removeIf { it.uid.toString() == uid } }
                _kickoutUserFlow.emit(Unit)
            }
        }
    }

    /**
     * 推出
     */
    fun exitRoom() {
        viewModelScope.launch {
            apiRequest {
                api.exitRoom(ChatroomInfoRequest(roomId)).checkAndGet()
            }.apiResponse {
                rtcHandler.exit()
                chatroomHandler.exit()
            }
        }
    }

    /**
     * 取消禁言
     */
    fun unsilence(uid: String = currentUserDetail?.id.toString()) {
        viewModelScope.launch {
            apiRequest {
                api.unmuted(
                    ChatroomUserRequest(uid = uid, roomId = roomId)
                ).checkAndGet()
                refreshCurrentUserInfo()
                muteList.handle { removeIf { it.uid.toString() == uid } }
            }.apiResponse()
        }
    }

    /**
     * 禁言
     */
    fun silence(type: Int) {
        val uid = currentUserDetail?.id.toString()
        viewModelScope.launch {
            apiRequest {
                api.silence(
                    ChatroomMutedRequest(type = type, uid = uid, roomId = roomId)
                ).checkAndGet()
                refreshCurrentUserInfo()
            }.apiResponse()
        }
    }

    /**
     * 拉人上麦/
     */
    fun upUserSeat() {
        val currentUserMikeInfo = currentUserMikeInfo
        viewModelScope.launch {
            if (currentUserMikeInfo != null) {
                return@launch
            }
            val emptyMike = firstEmptyMikeInfo
            if (emptyMike == null) {
                application.toast(R.string.room_not_empty_seat)
                return@launch
            }
            apiRequest {
                api.upUserSeat(
                    ChatroomUserSeatRequest(
                        seatId = emptyMike.id.toString(),
                        uid = currentUserDetail?.id.toString(),
                        roomId = roomId
                    )
                ).checkAndGet()
                refreshMikeInfos()
                refreshCurrentUserInfo()
            }.apiResponse()
        }
    }

    /**
     * 踢人下麦
     */
    fun downUserSeat() {
        val currentUserMikeInfo = currentUserMikeInfo
        viewModelScope.launch {
            if (currentUserMikeInfo == null) {
                return@launch
            }
            apiRequest {
                api.downUserSeat(
                    ChatroomUserSeatRequest(
                        seatId = currentUserMikeInfo.id.toString(),
                        uid = currentUserDetail?.id.toString(),
                        roomId = roomId
                    )
                ).checkAndGet()
                refreshMikeInfos()
                refreshCurrentUserInfo()
            }.apiResponse()
        }
    }

    /**
     * 关注或
     */
    fun follow() {
        viewModelScope.launch {
            apiRequest {
                api.followUser(UidRequest(_currentUserDetail?.id.toString()))
                    .checkAndGet()
                refreshCurrentUserInfo()
            }.apiResponse()
        }
    }

    fun unFollow() {
        viewModelScope.launch {
            apiRequest {
                api.unfollowUser(UidRequest(_currentUserDetail?.id.toString()))
                    .checkAndGet()
                refreshCurrentUserInfo()
            }.apiResponse()
        }
    }

    /**
     * 关注或
     */
    fun followRoom() {
        viewModelScope.launch {
            apiRequest {
                api.followRoom(ChatroomInfoRequest(roomId))
                    .checkAndGet()
            }.apiResponse {
                val newRoomInfo = _chatroomInfoResponse?.roomInfo?.copy(isFollow = 1)
                _chatroomInfoResponse = _chatroomInfoResponse?.copy(roomInfo = newRoomInfo)
            }
        }
    }

    fun unFollowRoom() {
        viewModelScope.launch {
            apiRequest {
                api.unfollowRoom(ChatroomInfoRequest(roomId))
                    .checkAndGet()
            }.apiResponse {
                val newRoomInfo = _chatroomInfoResponse?.roomInfo?.copy(isFollow = 0)
                _chatroomInfoResponse = _chatroomInfoResponse?.copy(roomInfo = newRoomInfo)
            }
        }
    }
}