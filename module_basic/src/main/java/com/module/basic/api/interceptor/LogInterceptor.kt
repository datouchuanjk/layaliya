package com.module.basic.api.interceptor

import android.content.SharedPreferences
import android.util.Log
import com.helper.develop.util.toJson
import com.module.basic.sp.getToken
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

/**
 * 这个只是用于输入
 */
class LogInterceptor(private val sp: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", sp.getToken())
            .build()
            .apply { logRequest(this) }
        val response = chain.proceed(request).apply { logResponse(this) }
        return response
    }

    private fun logRequest(request: Request) {
        val url = request.url.toString()
        val headers = request.headers.names().map {
            it to request.headers[it]
        }.toJson()
        val method = request.method
        val params = when (method) {
            "GET" -> {
                request.url.queryParameterNames.map {
                    it to request.url.queryParameter(it)
                }.toJson()
            }

            "POST" -> {
                val requestBody = request.body
                if (requestBody is FormBody) {
                    buildList {
                        for (i in 0..<requestBody.size) {
                            add(requestBody.name(i) to requestBody.value(i))
                        }
                    }.toJson()
                } else {
                    var charset = Charset.forName("UTF-8")
                    val contentType = requestBody?.contentType()
                    if (contentType != null) {
                        charset = contentType.charset(charset)
                    }
                    val buffer = Buffer()
                    requestBody?.writeTo(buffer)
                    buffer.readString(charset)
                }
            }

            else -> "Unit"
        }
        Log.e("HttpRequest", " url=$url \n headers=$headers \n params=$params")
    }

    private fun logResponse(response: Response) {
        val url = response.request.url
        val responseBody = response.body
        val source = responseBody?.source()
        source?.request(Long.MAX_VALUE) // Buffer the entire body.
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody?.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        val responseData = source?.buffer?.clone()?.readString(charset)
        Log.e("HttpResponse", "url=$url \n responseData=$responseData")
    }
}