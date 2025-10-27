package com.module.chatroom.module

import com.module.chatroom.api.service.ChatroomApiService
import com.module.chatroom.viewmodel.ChatRoomViewModel
import com.module.chatroom.viewmodel.ChatroomEnterCheckViewModel
import com.module.chatroom.viewmodel.ChatroomReportViewModel
import com.module.chatroom.viewmodel.ChatroomUserListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val chatroomModule = module {

    single {
        get<Retrofit>().create(ChatroomApiService::class.java)
    }

    viewModel {
        ChatroomReportViewModel(get(), get(),get())
    }

    viewModel {
        ChatRoomViewModel(get(), get(),get(),get())
    }

    viewModel{
        ChatroomUserListViewModel(get(), get())
    }
    viewModel {
         ChatroomEnterCheckViewModel(get(),get())
    }
}