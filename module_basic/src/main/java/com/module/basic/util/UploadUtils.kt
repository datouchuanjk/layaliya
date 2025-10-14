package com.module.basic.util

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.amazonaws.auth.BasicSessionCredentials
import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.*
import com.helper.develop.util.copyAsFile
import com.module.basic.api.data.response.UploadConfigResponse
import com.module.basic.api.service.BasicApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.random.Random
import   com.amazonaws.regions.Region
import com.module.basic.api.ApiException
import com.module.basic.sp.AppGlobal

class UploadUtils(
    private val context: Context,
    private val api: BasicApiService,
) {
    private var _uploadConfigResponse: UploadConfigResponse? = null
    private var s3Client: AmazonS3Client? = null

    companion object {
        const val TAG_ZONE = "zone"
        const val TAG_ROOM = "room"
        const val TAG_AVATAR = "avatar"
    }

    suspend fun uploadFiles(
        tag: String,
        files: List<File>,
        canRetry: Boolean = true,
    ): List<String> = files.map {
        uploadFile(
            tag = tag,
            file = it,
            canRetry = canRetry
        )
    }

    suspend fun uploadFile(
        tag: String,
        file: File,
        canRetry: Boolean = true,
    ): String {
        return try {
            if (_uploadConfigResponse == null || s3Client == null) {
                _uploadConfigResponse = withContext(Dispatchers.IO) {
                    api.uploadConfig().apply {
                        if (!isSuccessful) {
                            throw ApiException(
                                code = code,
                                message = message
                            )
                        }
                    }.data?.apply {
                        s3Client = AmazonS3Client(
                            StaticCredentialsProvider(
                                BasicSessionCredentials(
                                    accessKeyId,
                                    secretAccessKey,
                                    sessionToken
                                )
                            ),
                            Region.getRegion("me-central-1")
                        )
                    } ?: throw Exception("api request failed")
                }
            }
            val response = _uploadConfigResponse ?: throw Exception("client is null")
            val client = s3Client ?: throw Exception("client is null")
            val key = "test/${tag}/${AppGlobal.userResponse?.id}/${file.name}"
            val request = PutObjectRequest(
                response.bucket,
                key,
                file
            )
            client.putObject(request)
            return "${response.domain}/${key}"
        } catch (e: Exception) {
            if (canRetry) {
                _uploadConfigResponse = null
                uploadFile(tag, file, false)
            } else {
                throw e
            }
        }
    }
}



