package com.module.login.module

import androidx.annotation.Keep
import com.module.login.api.service.LoginApiService
import com.module.login.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

@Keep
val loginModule = module {
    viewModel {
        LoginViewModel(get(), get(),get())
    }
    single {
        get<Retrofit>().create(LoginApiService::class.java)
    }
}