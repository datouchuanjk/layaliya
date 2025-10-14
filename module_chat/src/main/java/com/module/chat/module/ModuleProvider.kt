package com.module.chat.module


import com.module.chat.api.service.*
import com.module.chat.viewmodel.ChatViewModel
import com.module.chat.viewmodel.ConversationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val chatModule = module {
    single {
        get<Retrofit>().create(ChatApiService::class.java)
    }
    viewModel {
        ConversationViewModel()
    }
    viewModel {
        ChatViewModel(get(),get())
    }
}