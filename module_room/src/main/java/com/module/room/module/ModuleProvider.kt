package com.module.room.module

import com.module.room.api.service.RoomApiService
import com.module.room.viewmodel.CreateOrEditRoomViewModel
import com.module.room.viewmodel.MyRoomViewModel
import com.module.room.viewmodel.RoomCheckViewModel
import com.module.room.viewmodel.RoomViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val roomModule = module {

    single {
        get<Retrofit>().create(RoomApiService::class.java)
    }

    viewModel {
        val type = it.get<Int>(0)
        RoomViewModel(type, get())
    }

    viewModel {
        val type = it.get<Int>(0)
        MyRoomViewModel(type, get())
    }
    viewModel {
        CreateOrEditRoomViewModel(get(), get(),get())
    }

    viewModel{
        RoomCheckViewModel(get())
    }
}