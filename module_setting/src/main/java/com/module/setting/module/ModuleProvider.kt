package com.module.setting.module

import com.module.setting.api.service.SettingApiService
import com.module.setting.viewmodel.SettingViewModel
import org.koin.androidx.viewmodel.dsl.*
import org.koin.dsl.module
import retrofit2.Retrofit

val settingModule = module {
    single {
        get<Retrofit>().create(SettingApiService::class.java)
    }
    viewModel {
        SettingViewModel(get())
    }
}