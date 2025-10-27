package com.module.community.module

import com.module.community.api.service.CommunityApiService
import com.module.community.viewmodel.CommentViewModel
import com.module.community.viewmodel.CommunityDetailViewModel
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
        CommunityDetailViewModel(get(),get())
    }
    viewModel {
        val id = it.get<String>(0)
        CommentViewModel(id,get())
    }
    viewModel {
        PostCommunityViewModel(get(),get())
    }
    single {
        get<Retrofit>().create(CommunityApiService::class.java)
    }
}