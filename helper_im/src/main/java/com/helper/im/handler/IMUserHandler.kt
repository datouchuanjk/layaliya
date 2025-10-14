package com.helper.im.handler

import android.util.Log
import com.helper.im.data.IMConversation
import com.helper.im.data.IMUser
import com.helper.im.data.transform
import com.helper.im.util.logIM
import com.netease.nimlib.sdk.v2.user.V2NIMUser
import com.netease.nimlib.sdk.v2.user.V2NIMUserListener
import com.netease.nimlib.sdk.v2.user.V2NIMUserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class IMUserHandler internal constructor(scope: CoroutineScope) :
    Handler<V2NIMUserService>(scope), V2NIMUserListener {

    init {
        register {
            service.addUserListener(this)
            return@register {
                service.removeUserListener(this)
            }
        }
    }

    private val _userProfileChangedFlow = MutableSharedFlow<List<IMUser>>()
    val userProfileChangedFlow = _userProfileChangedFlow.asSharedFlow()
    override fun onUserProfileChanged(users: MutableList<V2NIMUser>?) {
        logIM("onUserProfileChanged users->${users}")
        users ?: return
        if (users.isEmpty()) return
        launch {
            _userProfileChangedFlow.emit(users.map { it.transform() })
        }
    }

    override fun onBlockListAdded(user: V2NIMUser?) {
        logIM("onBlockListAdded user->${user}")
    }

    override fun onBlockListRemoved(accountId: String?) {
        logIM("onBlockListRemoved accountId->${accountId}")
    }

    /**
     *有新会话 那么我会冲服务器拉一次
     */
    internal fun refreshUserInfos(accountId: String) {
        service.getUserListFromCloud(listOf(accountId), {}, {})
    }

    /**
     * 更新一次
     */
    internal fun refreshUserInfos(accountIds: List<String>) {
        service.getUserListFromCloud(accountIds, {}, {})
    }

    /**
     * 获取本地用户信息
     */
    fun getLocalUserInfo(accountId: String?,refreshWhenNull: Boolean=true): IMUser? {
        accountId?:return null
        return service.getUserInfo(accountId).data?.transform().apply {
            if(this==null&&refreshWhenNull){
                refreshUserInfos(accountId)
            }
        }
    }
}