package com.module.gift.module

import com.module.gift.api.service.GiftApiService
import com.module.gift.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val giftModule = module {
    single {
        get<Retrofit>().create(GiftApiService::class.java)
    }

    viewModel {
        GiftViewModel(get(),get())
    }
}
