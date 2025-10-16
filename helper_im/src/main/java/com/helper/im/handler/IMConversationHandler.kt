package com.helper.im.handler

import com.helper.develop.paging.LoadResult
import com.helper.develop.paging.PagingConfig
import com.helper.develop.paging.PagingStart
import com.helper.develop.paging.buildPaging
import com.helper.im.IMHelper
import com.helper.im.data.transform
import com.helper.im.util.logIM
import com.helper.im.transform
import com.helper.im.util.toTargetId
import com.netease.nimlib.sdk.v2.V2NIMError
import com.netease.nimlib.sdk.v2.conversation.V2NIMLocalConversationListener
import com.netease.nimlib.sdk.v2.conversation.V2NIMLocalConversationService
import com.netease.nimlib.sdk.v2.conversation.model.V2NIMLocalConversation
import com.netease.nimlib.sdk.v2.conversation.params.V2NIMLocalConversationFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.*
import kotlin.collections.forEachIndexed
import kotlin.collections.removeAll
import kotlin.coroutines.resume

/**
 * 会话帮助类
 */
 class IMConversationHandler internal constructor(scope: CoroutineScope) :
    Handler<V2NIMLocalConversationService>(scope),
    V2NIMLocalConversationListener {

    override fun onSyncStarted() {
        logIM("onSyncStarted")
    }

    override fun onSyncFinished() {
        logIM("onSyncFinished")
        pagingData.refresh()
    }

    override fun onSyncFailed(error: V2NIMError?) {
        logIM("onSyncFailed error->${error}")
    }

    override fun onConversationCreated(conversation: V2NIMLocalConversation?) {
        logIM("onConversationCreated conversation=${conversation}")
        conversation ?: return
        IMHelper.userHandler.refreshUserInfos(conversation.conversationId.toTargetId())
        pagingData.handle {
            add(0, conversation.transform())
        }

    }

    override fun onConversationDeleted(conversationIds: MutableList<String>?) {
        logIM("onConversationDeleted conversationIds->${conversationIds}")
        conversationIds ?: return
        if (conversationIds.isEmpty()) return
        pagingData.handle {
            removeAll {
                it.conversationId in conversationIds
            }
        }
    }

    override fun onConversationChanged(conversationList: MutableList<V2NIMLocalConversation>?) {
        logIM("onConversationChanged conversationList->${conversationList}")
        conversationList ?: return
        if (conversationList.isEmpty()) return
        pagingData.handle {
            removeAll {
                it.conversationId in conversationList.map { it.conversationId }
            }
            addAll(0, conversationList.map { it.transform() })
        }
    }

    private val _totalUnreadCountChanged = MutableStateFlow(0)
    val totalUnreadCountChanged = _totalUnreadCountChanged.asStateFlow()
    override fun onTotalUnreadCountChanged(unreadCount: Int) {
        logIM("onTotalUnreadCountChanged unreadCount->${unreadCount}")
        _totalUnreadCountChanged.value = unreadCount
    }

    override fun onUnreadCountChangedByFilter(
        filter: V2NIMLocalConversationFilter?,
        unreadCount: Int
    ) {
        logIM("onUnreadCountChangedByFilter filter->${filter} unreadCount->${unreadCount}")
    }

    override fun onConversationReadTimeUpdated(conversationId: String?, readTime: Long) {
        logIM("onConversationReadTimeUpdated conversationId->${conversationId} readTime->${readTime}")
    }

    init {
        register {
            service.addConversationListener(this)
            return@register {
                service.removeConversationListener(this)
            }
        }
    }


    fun clearUnreadCountById(conversationId: String) {
        logIM("clearUnreadCountById conversationId->${conversationId}")
        service.clearUnreadCountByIds(listOf(conversationId), {}, {})
    }

    val pagingData = buildPaging(
        coroutineScope = this,
        pagingStart = PagingStart.LAZY,
        initialKey = 0L,
        config = PagingConfig(pageSize = 50)
    ) { loadParams ->
        val result = suspendCancellableCoroutine { continuation ->
            service.getConversationList(loadParams.key ?: 0L, loadParams.pageSize, {
                continuation.resume(it)
            }, {
                throw it.transform()
            })
        }
        LoadResult(
            if (result.isFinished) null else result.offset,
            result.conversationList.map { it.transform()}
                .apply {
                    IMHelper.userHandler.refreshUserInfos(this.map { it.targetId })
                }
        )
    }.pagingData

    init {
        launch {
            IMHelper.userHandler.userProfileChangedFlow.collect { users ->
                pagingData.handle {
                    users.forEach { user ->
                        this.forEachIndexed { index, item ->
                            if (user.accountId == item.targetId) {
                                this[index] = item.copy(name = user.name, avatar = user.avatar)
                            }
                        }
                    }
                }
            }
        }
    }
}