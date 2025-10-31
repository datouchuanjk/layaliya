package com.helper.im

import android.app.Application
import android.content.Context
import com.helper.im.handler.*
import com.helper.im.util.logIM
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.SDKOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope

object IMHelper : CoroutineScope by MainScope() {

    internal lateinit var context: Application

   internal val isDebug =true
    fun initV2(context: Application) {
        logIM("init context=$context")
        IMHelper.context = context
        NIMClient.initV2(context, SDKOptions().apply {
            enableV2CloudConversation = true
            appKey = "f1448863123894e523ef0c8411059eac"
        })
    }

    /**
     * 全局单例
     */
    val loginHandler by lazy { IMLoginHandler(this) }

    /**
     * 全局单例
     */
    val conversationHandler by lazy { IMConversationHandler(this) }

    /**
     * 全局单例
     */
    val userHandler by lazy { IMUserHandler(this) }

    /**
     * 全局单例
     */
    val systemMessageHandler by lazy { IMSystemMessageHandler(this) }

    /**
     * 全局单例
     */
    val notificationMessageHandler by lazy { IMNotificationMessageHandler(this) }


    internal val conversationIds = mutableListOf<String>()

    /**
     *  消息里面 是和会话绑定在一起的，比如说列表 就是某个会话的列表，监听也是监听某个会话的消息
     */
    fun messageHandler(scope: CoroutineScope, conversationId: String): IMMessageHandler {
        return IMMessageHandler(scope, conversationId).apply {
            conversationIds.add(conversationId)
            coroutineContext[Job]?.invokeOnCompletion {
                conversationIds.remove(conversationId)
            }
        }
    }

    fun chatroomHandler(scope: CoroutineScope): IMChatroomHandler {
        return IMChatroomHandler(scope)
    }

    fun rtcHandler(
        context: Context,
        scope: CoroutineScope,
    ): RTCHandler {
        return RTCHandler(context, scope)
    }
}