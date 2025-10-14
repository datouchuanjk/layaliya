package com.helper.im.handler

import android.content.Context
import android.util.Log
import com.netease.lava.nertc.sdk.NERtcCallback
import com.netease.lava.nertc.sdk.NERtcConstants
import com.netease.lava.nertc.sdk.NERtcEx
import com.netease.lava.nertc.sdk.NERtcParameters
import com.netease.lava.nertc.sdk.NERtcUserJoinExtraInfo
import com.netease.lava.nertc.sdk.NERtcUserLeaveExtraInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RTCHandler(context: Context, scope: CoroutineScope) : Handler<Unit>(scope), NERtcCallback {

    private val isEmulator = false

    companion object {
        private const val KEY = "f1448863123894e523ef0c8411059eac"
    }

    private val _userAudioStart = MutableSharedFlow<Long>()
    val userAudioStart = _userAudioStart.asSharedFlow()
    private val _userAudioStop = MutableSharedFlow<Long>()
    val userAudioStop = _userAudioStop.asSharedFlow()
    private val _joinFailedFlow = MutableSharedFlow<Unit>()
    val joinFailedFlow = _joinFailedFlow.asSharedFlow()

    init {
        register {
            if (isEmulator) return@register {}
            val parameters = NERtcParameters()
            parameters.setBoolean(NERtcParameters.KEY_AUTO_SUBSCRIBE_AUDIO, true)
            NERtcEx.getInstance().setParameters(parameters)
            NERtcEx.getInstance().init(context, KEY, this, null)
            return@register {}
        }
    }

    override fun onUserAudioStart(uid: Long) {
        launch {
            _userAudioStart.emit(uid)
        }
    }


    override fun onUserAudioStop(uid: Long) {
        launch {
            _userAudioStop.emit(uid)
        }
    }

    fun enter(token: String, channelName: String, uid: Long) {
        if (isEmulator) return
        Log.e("1234","进入RTC token=$token channelName=$channelName  uid =$uid" )
        NERtcEx.getInstance().joinChannel(token, channelName, uid)
    }

    fun exit() {
        if (isEmulator) return
        NERtcEx.getInstance().leaveChannel()
        NERtcEx.getInstance().release()
    }

    fun open() {
        if (isEmulator) return
        NERtcEx.getInstance().isRecordDeviceMute = false
    }

    fun close() {
        if (isEmulator) return
        NERtcEx.getInstance().isRecordDeviceMute = true
    }

    override fun onJoinChannel(
        result: Int,
        channelId: Long,
        elapsed: Long,
        uid: Long
    ) {
        Log.e("1234","进入RTC result=$result channelId=$channelId elapsed=$elapsed uid =$uid" )
        if (result != NERtcConstants.ErrorCode.OK) {
            launch {
                _joinFailedFlow.emit(Unit)
            }
        }
    }
















    override fun onLeaveChannel(result: Int) {

    }

    override fun onUserJoined(
        uid: Long,
        joinExtraInfo: NERtcUserJoinExtraInfo?
    ) {
    }


    override fun onUserLeave(
        uid: Long,
        reason: Int,
        leaveExtraInfo: NERtcUserLeaveExtraInfo?
    ) {

    }


    override fun onUserVideoStart(uid: Long, maxProfile: Int) {
    }

    override fun onUserVideoStop(uid: Long) {
    }

    override fun onDisconnect(reason: Int) {
    }

    override fun onClientRoleChange(oldRole: Int, newRole: Int) {
    }


    @Deprecated("Deprecated in Java")
    override fun onUserJoined(uid: Long) {

    }

    @Deprecated("Deprecated in Java")
    override fun onUserLeave(uid: Long, reason: Int) {

    }
}