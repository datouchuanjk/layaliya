package com.module.basic.api.data.response

import androidx.annotation.Keep


@Keep
data class UploadConfigResponse(
    val accessKeyId: String?,
    val expiration: String?,
    val secretAccessKey: String?,
    val sessionToken: String?,
    val domain: String?,
    val bucket: String?
)
