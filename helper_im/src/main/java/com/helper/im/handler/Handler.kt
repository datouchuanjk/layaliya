package com.helper.im.handler

import com.netease.nimlib.sdk.NIMClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.lang.reflect.ParameterizedType

  abstract class Handler<T>(private val scope: CoroutineScope) : CoroutineScope by scope {
    @Suppress("UNCHECKED_CAST")
    protected val service by lazy {
        val type = this::class.java.genericSuperclass as ParameterizedType
        val tClass = type.actualTypeArguments[0] as Class<T>
        NIMClient.getService(tClass)!!
    }

    fun register(block: () -> () -> Unit) {
        val invokeOnCompletion = block()
        coroutineContext[Job]?.invokeOnCompletion {
            invokeOnCompletion()
        }
    }
}