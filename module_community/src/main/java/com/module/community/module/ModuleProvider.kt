package com.module.community.module

import com.module.community.api.service.CommunityApiService
import com.module.community.viewmodel.CommunityViewModel
import com.module.community.viewmodel.PostCommunityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val communityModule = module {
    viewModel {
        CommunityViewModel(get())
    }
    viewModel {
        PostCommunityViewModel(get(),get())
    }
    single {
        get<Retrofit>().create(CommunityApiService::class.java)
    }
}