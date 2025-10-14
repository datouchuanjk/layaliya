package com.module.store.module

import com.module.store.api.service.StoreApiService
import com.module.store.viewmodel.*
import org.koin.androidx.viewmodel.dsl.*
import org.koin.core.scope.*
import org.koin.dsl.module
import retrofit2.Retrofit

val storeModule = module {
    single {
        get<Retrofit>().create(StoreApiService::class.java)
    }

    viewModel {
        StoreViewModel(get())
    }
}