package com.module.game.module

import androidx.compose.runtime.Composable
import com.module.basic.provider.GameScreenProvider
import com.module.game.viewmodel.GameViewModel
import com.module.game.api.service.GameApiService
import com.module.game.viewmodel.GameListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val gameModule = module {
    single {
        get<Retrofit>().create(GameApiService::class.java)
    }

    viewModel {
        GameListViewModel(get(), get())
    }

    viewModel {
        GameViewModel(get(),get(),get())
    }

    single<GameScreenProvider> {
        object: GameScreenProvider{
            @Composable
            override fun GameScreen() {
                com.module.game.ui.GameListScreenOnly()
            }
        }
    }
}
