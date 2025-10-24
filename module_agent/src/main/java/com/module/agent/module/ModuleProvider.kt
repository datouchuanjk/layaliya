package com.module.agent.module

import com.module.agent.api.service.AgentApiService
import com.module.agent.viewmodel.*
import com.module.agent.viewmodel.BasicInformationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val agentModule = module {
    single {
        get<Retrofit>().create(AgentApiService::class.java)
    }
    viewModel {
        BasicInformationViewModel(get(),get(),get())
    }
    viewModel {
        IdolListViewModel(get())
    }
    viewModel {
        BillViewModel(get())
    }

    viewModel {
        AdminViewModel(get())
    }
    viewModel {
        BDViewModel(get())
    }
    viewModel {
        CoinMerchantViewModel(get(),get())
    }
    viewModel {
        CoinDetailViewModel(get())
    }
}