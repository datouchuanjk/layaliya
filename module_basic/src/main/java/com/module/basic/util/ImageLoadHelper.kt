package com.module.basic.util

import android.annotation.SuppressLint
import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ImageLoadHelper(context: Context) {
    private val trustAllCerts = arrayOf<TrustManager>(
        @SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        }
    )

    val imageLoaderBuilder = ImageLoader.Builder(context)
        .okHttpClient(
            OkHttpClient.Builder()
                .hostnameVerifier { _, _ -> true } // 禁用主机名验证
                .sslSocketFactory(
                    SSLContext.getInstance("SSL").apply {
                        init(null, trustAllCerts, SecureRandom())
                    }.socketFactory,
                    trustAllCerts[0] as X509TrustManager
                )
                .build()
        )

}