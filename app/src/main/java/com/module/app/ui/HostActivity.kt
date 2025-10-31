package com.module.app.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.helper.develop.nav.*
import com.helper.develop.util.CustomActivityResultContracts
import com.helper.develop.util.createDensityContext
import com.helper.develop.util.createNotificationChannel
import com.helper.im.IMHelper
import com.module.agent.ui.adminScreen
import com.module.agent.ui.agentScreen
import com.module.agent.ui.bdScreen
import com.module.agent.ui.coinDetailScreen
import com.module.agent.ui.coinMerchantScreen
import com.module.app.viewmodel.HostViewModel
import com.module.bag.ui.*
import com.module.basic.constant.AppConstant
import com.module.basic.route.AppRoutes
import com.module.basic.sp.AppGlobal
import com.module.basic.sp.clearToken
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.base.bigImageScreen
import com.module.basic.ui.base.webViewScreen
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.charm.ui.*
import com.module.chat.ui.chatScreen
import com.module.community.ui.postCommunityScreen
import com.module.login.ui.loginScreen
import com.module.mine.ui.*
import com.module.chatroom.ui.chatRoomScreen
import com.module.chatroom.ui.chatroomEnterCheckDialog
import com.module.room.ui.createOrEditRoomScreen
import com.module.room.ui.myRoomScreen
import com.module.noble.ui.nobleHistoryScreen
import com.module.noble.ui.nobleScreen
import com.module.chatroom.ui.chatroomReportDialog
import com.module.chatroom.ui.chatroomUserListDialog
import com.module.community.ui.communityDetailScreen
import com.module.emoji.ui.emojiDialog
import com.module.game.ui.gameDialog
import com.module.game.ui.gameListScreen
import com.module.game.ui.gameScreen
import com.module.gift.ui.*
import com.module.noble.ui.dialog.explainScreen
import com.module.room.ui.roomCreateCheckDialog
import com.module.setting.ui.*
import com.module.store.ui.*
import com.module.wallet.ui.walletScreen
import com.module.wealth.ui.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get

/**
 * 全局唯一Activity  单Activity模式
 */
class HostActivity : ComponentActivity() {
    private var _navController: NavHostController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        enableEdgeToEdge()
        val sp = get<SharedPreferences>()
        IMHelper.initV2(application)
        lifecycleScope.launch {
            AppGlobal.exitFlow.collect {
                IMHelper.loginHandler.logout()
                sp.clearToken()
                val localNav = _navController ?: return@collect
                localNav.navigateAndPopAll(AppRoutes.Login.static)
            }
        }
        lifecycleScope.launch {
            IMHelper.loginHandler.onKickedOffline.collect {
                AppGlobal.exit()
            }
        }

        setContent {
            NavControllerLocalProvider { navController ->
                val viewModel: HostViewModel = apiHandlerViewModel()
                _navController = navController
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                ) {
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize(),
                        navController = navController,
                        startDestination = AppRoutes.Launcher.static
                    ) {
                        launcherScreen()
                        bigImageScreen()
                        mainScreen()
                        loginScreen()
                        myRoomScreen()
                        chatRoomScreen()
                        createOrEditRoomScreen()
                        personCenterScreen()
                        followersOrFansScreen()
                        chatScreen()
                        settingScreen()
                        storeScreen()
                        charmLevelScreen()
                        wealthLevelScreen()
                        nobleScreen()
                        nobleHistoryScreen()
                        agentScreen()
                        postCommunityScreen()
                        communityDetailScreen()
                        walletScreen()
                        adminScreen()
                        bdScreen()
                        giftDialog()
                        emojiDialog()
                        bagScreen()
                        personEditScreen()
                        coinMerchantScreen()
                        coinDetailScreen()
                        chatroomReportDialog()
                        chatroomUserListDialog()
                        roomCreateCheckDialog()
                        chatroomEnterCheckDialog()
                        giftPlayDialog()
                        gameListScreen()
                        gameScreen()
                        gameDialog()
                        webViewScreen()
                        explainScreen()
                    }
                    Column(
                        modifier = Modifier.statusBarsPadding()
                    ) {
                        viewModel.top1?.let {
                            key(it) {
                                TopPlay(it) {
                                    viewModel.top1 = viewModel.get()
                                }
                            }
                        }
                        viewModel.top2?.let {
                            key(it) {
                                TopPlay(it) {
                                    viewModel.top2 = viewModel.get()
                                }
                            }
                        }
                        viewModel.top3?.let {
                            key(it) {
                                TopPlay(it) {
                                    viewModel.top3 = viewModel.get()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    /**
     * 处理离线，后台
     */
    private fun handleIntent(intent: Intent) {
        when (intent.getStringExtra("action")) {
            AppConstant.NOTIFICATION_ACTION_TO_CHAT -> {
                val conversationId = intent.getStringExtra("conversationId")
                val senderName = intent.getStringExtra("senderName")
                _navController?.navigate(
                    AppRoutes.Chat.dynamic(
                        "conversationId" to conversationId,
                        "name" to senderName
                    )
                )
            }
        }
    }

    /**
     * 处理通知栏渠道与权限
     */
    private fun initNotification() {
        createNotificationChannel(AppConstant.CHANNEL_ID)
        registerForActivityResult(
            CustomActivityResultContracts.RequestNotificationPermission()
        ) {}.launch(AppConstant.CHANNEL_ID)
    }

    /**
     * 屏幕适配
     */
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.createDensityContext(375f))
    }
}








