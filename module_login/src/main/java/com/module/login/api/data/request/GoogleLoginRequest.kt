package com.module.login.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data  class GoogleLoginRequest(
    @SerializedName("id_token")
    val idToken:String
): BaseRequest()