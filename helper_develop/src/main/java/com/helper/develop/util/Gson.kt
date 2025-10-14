package com.helper.develop.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun Any.toJson(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String.fromJson(): T? {
    return try {
        Gson().fromJson(this, T::class.java)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T> String.fromTypeJson(): T? {
    return try {
        Gson().fromJson(this, object: TypeToken<T>(){}.type)
    } catch (e: Exception) {
        null
    }
}