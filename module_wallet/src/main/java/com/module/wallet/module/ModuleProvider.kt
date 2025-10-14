package com.module.wallet.module

import com.module.wallet.api.service.WalletApiService
import com.module.wallet.ui.*
import com.module.wallet.ui.Diamond
import com.module.wallet.viewmodel.*
import org.koin.androidx.viewmodel.dsl.*
import org.koin.dsl.module
import retrofit2.Retrofit

val walletModule = module {
    single {
        get<Retrofit>().create(WalletApiService::class.java)
    }

    viewModel {
        CoinsViewModel()
    }

    viewModel {
        DiamondViewModel()
    }
}
