package com.module.app

import com.module.agent.module.agentModule
import com.module.app.viewmodel.LauncherViewModel
import com.module.bag.module.*
import com.module.basic.module.basicModule
import com.module.basic.ui.BaseApplication
import com.module.charm.module.*
import com.module.chat.module.chatModule
import com.module.chatroom.module.chatroomModule
import com.module.community.module.communityModule
import com.module.room.module.roomModule
import com.module.login.module.loginModule
import com.module.comment.module.commentModule
import com.module.emoji.module.emojiModule
import com.module.gift.module.giftModule
import com.module.mine.module.mineModule
import com.module.noble.module.nobleModule
import com.module.setting.module.*
import com.module.store.module.*
import com.module.wallet.module.walletModule
import com.module.wealth.module.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                module {
                    viewModel {
                        LauncherViewModel(get(), get())
                    }
                },
                basicModule,
                loginModule,
                roomModule,
                communityModule,
                chatModule,
                mineModule,
                nobleModule,
                agentModule,
                giftModule,
                emojiModule,
                commentModule,
                walletModule,
                charmModule,
                wealthModule,
                storeModule,
                settingModule,
                bagModule,
                chatroomModule
            )
        }
    }
}