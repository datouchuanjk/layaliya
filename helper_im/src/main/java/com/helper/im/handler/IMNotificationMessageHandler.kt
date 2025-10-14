package com.helper.im.handler


import com.helper.develop.util.getIntOrNull
import com.helper.develop.util.getJSONObjectOrNull
import com.helper.develop.util.getStringOrNull
import com.netease.nimlib.sdk.v2.notification.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.json.JSONObject

class IMNotificationMessageHandler internal constructor(
    scope: CoroutineScope,
) : Handler<V2NIMNotificationService>(scope), V2NIMNotificationListener {

    private val _receiveMessagesFlow = MutableSharedFlow<String>()
    val receiveMessagesFlow = _receiveMessagesFlow.asSharedFlow()

    init {
        register {
            service.addNotificationListener(this)
            return@register {
                service.removeNotificationListener(this)
            }
        }
    }

    override fun onReceiveCustomNotifications(customNotifications: MutableList<V2NIMCustomNotification>?) {

    }

    override fun onReceiveBroadcastNotifications(broadcastNotifications: MutableList<V2NIMBroadcastNotification>?) {
        broadcastNotifications?.forEach {
            val jsonObject = JSONObject(it.content)
            if (jsonObject.getIntOrNull("code") == 1016) {
                jsonObject.getStringOrNull("data")?.let { data ->
                    launch {
                        _receiveMessagesFlow.emit(data)
                    }
                }
            }
        }
    }

}