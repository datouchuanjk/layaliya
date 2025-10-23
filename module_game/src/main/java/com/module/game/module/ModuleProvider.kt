package com.module.game.module

import androidx.compose.runtime.Composable
import com.module.basic.provider.GameScreenProvider
import com.module.game.viewmodel.GameViewModel
import com.module.game.api.service.GameApiService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val gameModule = module {
    single {
        get<Retrofit>().create(GameApiService::class.java)
    }

    viewModel {
        GameViewModel(get(),get())
    }
    single<GameScreenProvider> {
        object: GameScreenProvider{
            @Composable
            override fun GameScreen() {
                com.module.game.ui.GameScreen()
            }
        }
    }
}
