package com.helper.develop.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Process
import android.util.DisplayMetrics
import androidx.core.content.getSystemService

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

val Context.isMainProcess: Boolean get() = processName == packageName

val Context.processName: String?
    get() = getSystemService<ActivityManager>()
        ?.runningAppProcesses
        ?.find { it.pid == Process.myPid() }
        ?.processName

val Context.versionCode: Long
    get() = packageManager.getPackageInfo(packageName, 0).run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            longVersionCode
        } else {
            versionCode.toLong()
        }
    }

val Context.versionName: String
    get() = packageManager.getPackageInfo(packageName, 0).versionName.orEmpty()

fun Context.createDensityContext(value: Float): Context {
    val newDensityDpi = resources.displayMetrics.widthPixels
        .div(value)
        .times(DisplayMetrics.DENSITY_DEFAULT)
    val configuration = resources.configuration
    configuration.fontScale = 1f
    configuration.densityDpi = newDensityDpi.toInt()
    return createConfigurationContext(configuration)
}


