package com.module.basic.api.data.request

import android.os.Build
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.helper.develop.util.versionCode
import com.module.basic.sp.AppGlobal
import com.module.basic.ui.base.BaseApplication

@Keep
open class BaseRequest(
    @SerializedName("device_id")
//    val deviceId: String = AppGlobal.deviceId,
    val deviceId: String = "jiba mao ",
    val version: Long = BaseApplication.INSTANCE.versionCode,
    val system: Int = 1,
    @SerializedName("system_version")
    val systemVersion: String = Build.VERSION.RELEASE,
    @SerializedName("timestemp")
    val timesTemp: Long = System.currentTimeMillis(),
)