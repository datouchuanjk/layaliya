package com.module.basic.module

import android.content.Context
import android.content.SharedPreferences
import coil.ImageLoader
import com.module.basic.api.service.BasicApiService
import com.module.basic.api.interceptor.LogInterceptor
import com.module.basic.util.ImageLoadHelper
import com.module.basic.util.UploadUtils
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val basicModule = module {
    single {
        val packageName = androidContext().packageName
        androidContext().getSharedPreferences("${packageName}_preferences", Context.MODE_PRIVATE)
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.layaliya.com/api/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(LogInterceptor(get<SharedPreferences>()))
                    .build()
            ).addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ImageLoader> {
        get<ImageLoader.Builder>().build()
    }
    single<ImageLoader.Builder> {
        ImageLoadHelper(androidContext()).imageLoaderBuilder
    }

    single {
        get<Retrofit>().create(BasicApiService::class.java)
    }
    single<UploadUtils> {
        UploadUtils(get(), get())
    }
}


