package com.module.noble.module

import com.module.noble.api.service.NobleApiService
import com.module.noble.viewmodel.GiveToWhoViewModel
import com.module.noble.viewmodel.NobleHistoryViewModel
import com.module.noble.viewmodel.NobleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module
import retrofit2.Retrofit

val nobleModule = module {
    viewModel {
        NobleViewModel(get())
    }

    viewModel {
        GiveToWhoViewModel(get())
    }

    viewModel {
        NobleHistoryViewModel(get(),get(),get())
    }
    single {
        get<Retrofit>().create(NobleApiService::class.java)
    }
}