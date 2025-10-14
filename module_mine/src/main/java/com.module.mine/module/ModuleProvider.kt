package com.module.mine.module

import com.module.mine.api.service.MineApiService
import com.module.mine.viewmodel.FollowersOrFansViewModel
import com.module.mine.viewmodel.MineViewModel
import com.module.mine.viewmodel.PersonCenterViewModel
import com.module.mine.viewmodel.ProfileEditViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val mineModule = module {
    viewModel {
        MineViewModel()
    }

    viewModel {
        FollowersOrFansViewModel(get(), get())
    }

    viewModel {
        PersonCenterViewModel(get(), get(), get())
    }

    viewModel {
        ProfileEditViewModel(get(), get(), get(), get())
    }


    single {
        get<Retrofit>().create(MineApiService::class.java)
    }
}