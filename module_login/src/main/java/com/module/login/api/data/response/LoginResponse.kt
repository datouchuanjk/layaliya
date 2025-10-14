package com.module.login.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class LoginResponse(
    val token: String?,
)