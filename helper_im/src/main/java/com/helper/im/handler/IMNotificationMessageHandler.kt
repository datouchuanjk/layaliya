package com.helper.im.handler


import com.helper.develop.util.HMS
import com.helper.develop.util.YMD
import com.helper.develop.util.getIntOrNull
import com.helper.develop.util.getJSONObjectOrNull
import com.helper.develop.util.getStringOrNull
import com.netease.nimlib.sdk.v2.notification.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.json.JSONObject
import java.util.Calendar
import kotlin.random.Random

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

    fun test() {
        launch {
            _receiveMessagesFlow.emit(
                JSONObject()
                    .apply {
                        put(
                            "content",
                            "狗比1  ${Calendar.getInstance().HMS} ${Random.nextInt()}"
                        )
                    }
                    .toString()
            )
            _receiveMessagesFlow.emit(
                JSONObject()
                    .apply {
                        put(
                            "content",
                            "狗比2  ${Calendar.getInstance().HMS} ${Random.nextInt()}"
                        )
                    }
                    .toString()
            )
            _receiveMessagesFlow.emit(
                JSONObject()
                    .apply {
                        put(
                            "content",
                            "狗比3 ${Calendar.getInstance().HMS} ${Random.nextInt()}"
                        )
                    }
                    .toString()
            )
//            delay(5000)
//            _receiveMessagesFlow.emit(
//                JSONObject()
//                    .apply { put("content","狗比4 ${Calendar.getInstance().HMS}")}
//                    .toString()
//            )
//            delay(1000)
//            _receiveMessagesFlow.emit(
//                JSONObject()
//                    .apply { put("content","狗比5 ${Calendar.getInstance().HMS}")}
//                    .toString()
//            )
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
                        _receiveMessagesFlow.emit(
                            JSONObject(data)
                            .apply {
                                put(
                                    "currentTimeMillis",
                                    System.currentTimeMillis() + Random.nextInt()
                                )
                                //加这个的目的 是为了保证每个数据的唯一性，保证不会因为重组导致刷新
                            }
                            .toString())
                    }
                }
            }
        }
    }

}