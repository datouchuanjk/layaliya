package com.helper.im

import com.netease.nimlib.sdk.v2.V2NIMError
import okio.IOException

fun V2NIMError.transform() = IMException(
    code = code,
    desc = desc,
    detail = detail
)

class IMException(
    val code: Int,
    desc: String,
    val detail: MutableMap<String, Any>?
) : IOException(desc)