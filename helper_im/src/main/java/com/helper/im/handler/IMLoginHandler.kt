package com.helper.im.handler

import com.helper.im.IMHelper
import com.helper.im.util.logIM
import com.helper.im.transform
import com.netease.nimlib.sdk.v2.V2NIMError
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginDetailListener
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginListener
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginService
import com.netease.nimlib.sdk.v2.auth.enums.V2NIMConnectStatus
import com.netease.nimlib.sdk.v2.auth.enums.V2NIMDataSyncState
import com.netease.nimlib.sdk.v2.auth.enums.V2NIMDataSyncType
import com.netease.nimlib.sdk.v2.auth.enums.V2NIMLoginClientChange
import com.netease.nimlib.sdk.v2.auth.enums.V2NIMLoginStatus
import com.netease.nimlib.sdk.v2.auth.model.V2NIMKickedOfflineDetail
import com.netease.nimlib.sdk.v2.auth.model.V2NIMLoginClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class IMLoginHandler internal constructor(scope: CoroutineScope) : Handler<V2NIMLoginService>(scope), V2NIMLoginListener,
    V2NIMLoginDetailListener {

    override fun onLoginStatus(status: V2NIMLoginStatus?) {
        logIM("onLoginStatus status->${status}")
    }

    override fun onLoginFailed(error: V2NIMError?) {
        logIM("onLoginFailed error->${error}")
    }

    override fun onKickedOffline(detail: V2NIMKickedOfflineDetail?) {
        logIM("onKickedOffline detail->${detail}")
    }

    override fun onLoginClientChanged(
        change: V2NIMLoginClientChange?,
        clients: MutableList<V2NIMLoginClient>?
    ) {
        logIM("onLoginClientChanged change->${change}  clients=${clients}")
    }

    override fun onConnectStatus(status: V2NIMConnectStatus) {
        logIM("onConnectStatus status->${status}")
    }

    override fun onDisconnected(error: V2NIMError) {
        logIM("onDisconnected error->${error}")
    }

    override fun onConnectFailed(error: V2NIMError) {
        logIM("onConnectFailed error->${error}")
    }

    override fun onDataSync(
        type: V2NIMDataSyncType?,
        state: V2NIMDataSyncState?,
        error: V2NIMError?
    ) {
        logIM("onDataSync type->$type state->$state error->$error")
    }

    init {
        register {
            service.addLoginListener(this)
            service.addLoginDetailListener(this)
            return@register {
                service.removeLoginListener(this)
                service.removeLoginDetailListener(this)
            }
        }
    }

    suspend fun login(
        account: String,
        token: String,
    ) {
        suspendCancellableCoroutine { continuation ->
            service.login(
                account,
                token,
                null,
                {
                    continuation.resume(Unit)
                },
                {
                    continuation.resumeWithException(it.transform())
                }
            )
        }
    }
}