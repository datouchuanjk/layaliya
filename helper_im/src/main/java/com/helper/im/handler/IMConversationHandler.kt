package com.helper.im.handler

import com.helper.develop.paging.*
import com.helper.im.*
import com.helper.im.data.*
import com.helper.im.util.*
import com.netease.nimlib.sdk.*
import com.netease.nimlib.sdk.event.*
import com.netease.nimlib.sdk.event.model.*
import com.netease.nimlib.sdk.v2.*
import com.netease.nimlib.sdk.v2.conversation.*
import com.netease.nimlib.sdk.v2.conversation.model.*
import com.netease.nimlib.sdk.v2.conversation.params.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.*


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

    fun setCurrentConversation(conversationId: String?) {
            service.setCurrentConversation(conversationId)
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
        sub(result.conversationList.map { it.conversationId.toTargetId() })
        LoadResult(
            if (result.isFinished) null else result.offset,
            result.conversationList.map { it.transform() }
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

    fun sub(ids: List<String>) {
        NIMClient.getService(EventSubscribeService::class.java).subscribeEvent(
            EventSubscribeRequest().apply {
                eventType = 1
                expiry = 1L
                publishers = ids
            }
        ).setCallback(null)
        NIMClient.getService(EventSubscribeService::class.java).subscribeEvent(
            EventSubscribeRequest().apply {
                eventType = 2
                expiry = 30 * 24 * 60 * 60 * 1000
                publishers = ids
            }
        ).setCallback(null)
        NIMClient.getService(EventSubscribeService::class.java).subscribeEvent(
            EventSubscribeRequest().apply {
                eventType = 3
                expiry = 1L
                publishers = ids
            }
        ).setCallback(null)
        NIMClient.getService(EventSubscribeServiceObserver::class.java).observeEventChanged({
            val result = it ?: return@observeEventChanged
            result.forEach { item ->
                val online = item.eventType == 1
                pagingData.handle {
                    forEachIndexed { index, pagingItem ->
                        if (pagingItem.targetId == item.publisherAccount) {
                            set(index, pagingItem.copy(online = online))
                        }
                    }
                }
            }
        }, true)
    }
}


