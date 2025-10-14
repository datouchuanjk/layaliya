package com.module.bag.module

import com.module.bag.api.service.BagApiService
import com.module.bag.viewmodel.BagViewModel
import org.koin.androidx.viewmodel.dsl.*
import org.koin.dsl.module
import retrofit2.Retrofit

val bagModule = module {
    single {
        get<Retrofit>().create(BagApiService::class.java)
    }

    viewModel {
        BagViewModel(get())
    }
}