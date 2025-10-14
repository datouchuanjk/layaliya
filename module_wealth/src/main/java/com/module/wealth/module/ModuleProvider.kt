package com.module.wealth.module

import com.module.wealth.api.service.WealthApiService
import com.module.wealth.viewmodel.WealthLevelViewModel
import org.koin.androidx.viewmodel.dsl.*
import org.koin.dsl.module
import retrofit2.Retrofit

val wealthModule = module {

    viewModel {
        WealthLevelViewModel(get())
    }

    single {
        get<Retrofit>().create(WealthApiService::class.java)
    }
}
