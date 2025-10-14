package com.module.basic.sp

import android.content.SharedPreferences
import androidx.core.content.edit


fun SharedPreferences.putToken(value: String) {
    edit {
        putString("token", value)
    }
}

fun SharedPreferences.clearToken() {
    this.edit {
        this.remove("token")
    }
}

fun SharedPreferences.getToken(): String {
    return  getString("token", "").orEmpty()
}

