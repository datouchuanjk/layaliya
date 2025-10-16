package com.module.app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.helper.develop.nav.*
import com.helper.develop.util.CustomActivityResultContracts
import com.helper.develop.util.KeyboardHeightFlow
import com.helper.develop.util.createDensityContext
import com.helper.develop.util.createNotificationChannel
import com.helper.im.IMHelper
import com.module.agent.ui.adminScreen
import com.module.agent.ui.agentScreen
import com.module.agent.ui.bdScreen
import com.module.agent.ui.coinDetailScreen
import com.module.agent.ui.coinMerchantScreen
import com.module.bag.ui.*
import com.module.basic.constant.AppConstant
import com.module.basic.route.AppRoutes
import com.module.basic.util.LocalKeyboardHeight
import com.module.charm.ui.*
import com.module.chat.ui.chatScreen
import com.module.comment.ui.commentDialog
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
import com.module.emoji.ui.emojiDialog
import com.module.gift.ui.*
import com.module.noble.ui.noblePlayDialog
import com.module.room.ui.roomCreateCheckDialog
import com.module.setting.ui.*
import com.module.store.ui.*
import com.module.wallet.ui.walletScreen
import com.module.wallet.util.PayHelper
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
        initNotification()
        IMHelper.initV2(application)
        get<PayHelper>()
        lifecycleScope.launch {
            IMHelper.giftMessageHandler.receiveMessagesFlow.collect {
                val localNav = _navController ?: return@collect
                localNav.waitPopBackStack(AppRoutes.GiftPlay.static)
                localNav.navigate(
                    AppRoutes.GiftPlay.dynamic(
                        "json" to it
                    )
                )
            }
        }
        lifecycleScope.launch {
            IMHelper.notificationMessageHandler.receiveMessagesFlow.collect {
                val localNav = _navController ?: return@collect
                localNav.waitPopBackStack(AppRoutes.NoblePlay.static)
                localNav.navigate(
                    AppRoutes.NoblePlay.dynamic(
                        "json" to it
                    )
                )
            }
        }

        setContent {
            val localKeyboardHeight by KeyboardHeightFlow.collectAsState()
            NavControllerLocalProvider { navController ->
                CompositionLocalProvider(LocalKeyboardHeight provides with(LocalDensity.current) {
                    localKeyboardHeight.toDp()
                }) {
                    _navController = navController
                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.Launcher.static
                    ) {
                        launcherScreen()
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
                        walletScreen()
                        adminScreen()
                        bdScreen()
                        commentDialog()
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
                        noblePlayDialog()
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








