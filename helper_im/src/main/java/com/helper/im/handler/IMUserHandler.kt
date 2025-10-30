package com.helper.im.handler

import android.util.Log
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

/**
 * 用户信息处理器
 */
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
        users ?: return
        if (users.isEmpty()) return
        launch {
            _userProfileChangedFlow.emit(users.map { it.transform() })
        }
    }

    override fun onBlockListAdded(user: V2NIMUser?) {

    }

    override fun onBlockListRemoved(accountId: String?) {

    }


     fun refreshUserInfos(accountId: String?) {
        accountId ?: return
        service.getUserListFromCloud(listOf(accountId), {}, {})
    }

    /**
     * 更新一次
     */
     fun refreshUserInfos(accountIds: List<String>) {
        service.getUserListFromCloud(accountIds, {}, {})
    }

    /**
     * 获取本地用户信息
     */
    fun getLocalUserInfo(accountId: String?): IMUser? {
        accountId ?: return null
        return service.getUserInfo(accountId).data?.transform().apply {
            if (this == null) {
                Log.e("1234", "本地没有 ${accountId} 开始拉去服务器 ")
                refreshUserInfos(accountId)
            } else {
                Log.e("1234", "本地有 ${accountId}的信息 ")
            }
        }
    }


}