package com.module.comment.module

import com.module.comment.api.service.CommentApiService
import com.module.comment.viewmodel.CommentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val commentModule = module {
    viewModel {
        CommentViewModel(get(), get())
    }
    single {
        get<Retrofit>().create(CommentApiService::class.java)
    }
}