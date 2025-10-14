package com.module.basic.sp

import android.content.SharedPreferences
import androidx.core.content.edit

fun SharedPreferences.putHiddenIdentity(value: Boolean) {
    edit {
        putBoolean("putHiddenIdentity", value)
    }
}

fun SharedPreferences.getHiddenIdentity(): Boolean {
    return getBoolean("putHiddenIdentity", true)
}

