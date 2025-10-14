package com.module.emoji.module

import com.module.emoji.viewmodel.EmojiViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val emojiModule = module {

    viewModel {
        EmojiViewModel(get())
    }
}
