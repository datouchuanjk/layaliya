package com.module.charm.module

import com.module.charm.api.service.*
import com.module.charm.viewmodel.CharmLevelViewModel
import org.koin.androidx.viewmodel.dsl.*
import org.koin.dsl.module
import retrofit2.Retrofit

val charmModule = module {

    viewModel {
        CharmLevelViewModel(get())
    }
    single {
        get<Retrofit>().create(CharmApiService::class.java)
    }
}
