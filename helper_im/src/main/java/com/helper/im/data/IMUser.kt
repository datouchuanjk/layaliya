package com.helper.im.data

import androidx.annotation.Keep
import com.helper.develop.util.getStringOrNull
import com.netease.nimlib.sdk.v2.user.V2NIMUser
import org.json.JSONObject

internal fun V2NIMUser.transform() = IMUser(this)

@Keep
data class IMUser(
    private val v2NIMUser: V2NIMUser,
    val name: String? = v2NIMUser.name,
    val avatar: String? = v2NIMUser.avatar,
    val accountId: String = v2NIMUser.accountId
) {
    private val json by lazy {
        try {
            JSONObject(v2NIMUser.serverExtension)
        } catch (e: Exception) {
            null
        }
    }

    val uid get() = json?.getStringOrNull("uid")
}