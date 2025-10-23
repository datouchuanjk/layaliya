package com.module.game.ui

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import android.view.*
import android.webkit.*
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.*
import androidx.core.net.toUri
import androidx.navigation.*
import androidx.navigation.compose.*
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.navigateForResult
import com.module.basic.route.*
import com.module.basic.sp.AppGlobal
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.game.viewmodel.GameViewModel


fun NavGraphBuilder.gameScreen() = composable(
    route = AppRoutes.Game.static,
    arguments = AppRoutes.Game.arguments,
) {
    GameScreen()
}

@Composable
internal fun GameScreen(viewModel: GameViewModel = apiHandlerViewModel()) {
    val localNav = LocalNavController.current
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                settings.domStorageEnabled = true
                settings.databaseEnabled = true
                settings.mediaPlaybackRequiresUserGesture = true
                setLayerType(View.LAYER_TYPE_HARDWARE, null)
                webViewClient = object : WebViewClient() {
                    // 拦截所有链接，在当前 WebView 加载
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        // 允许当前 WebView 加载该链接（返回 false 表示不拦截，由 WebView 自行处理）
                        return false
                    }

                    // 兼容旧版本（API < 21）
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        return false
                    }
                }
//                addJavascriptInterface(JS(this, it as Activity), "LingxianAndroid")
                val gameUrl = "https://www.doubao.com/chat/"
                val newGameUrl = gameUrl.toUri().buildUpon()
                    .appendQueryParameter("uid", AppGlobal.userResponse?.id.toString())
                    .appendQueryParameter("token", viewModel.token)
                    .appendQueryParameter("lang", "en-US")
                    .appendQueryParameter("roomId", viewModel.roomId ?: "0")
                    .build().toString()
                loadUrl(newGameUrl)
            }
        }
    )
}


class JS(
    val webView: WebView,
    val context: Activity,
    val withChildScreen: Boolean,
    val localNav: NavHostController,
    val localBack: OnBackPressedDispatcherOwner?
) {

    /**
     * 关闭游戏 回退 如果有上级回到上级 否者退出界面 如果是嵌套界面，不能退出界面
     */
    @JavascriptInterface
    fun closeGame() {
        Log.e("Game", "closeGame被调用")
        context.runOnUiThread {
            if (webView.canGoBack()) {
                Log.e("Game", "有上一级界面，回到上级")
                webView.goBack()
            } else {
                if (withChildScreen) {
                    Log.e("Game", "没有上级界面，关闭界面，但是当前页面是子界面，直接return")
                    return@runOnUiThread
                }
                Log.e("Game", "没有上级界面，关闭界面S")
                localBack?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    /**
     * 游戏加载完成  不处理
     */
    @JavascriptInterface
    fun loadComplete() {
        Log.e("Game", "loadComplete被调用 什么都不做")
    }

    /**
     * 充值
     */
    @JavascriptInterface
    fun pay() {
        Log.e("Game", "pay被调用 去充值界面")
        context.runOnUiThread {
            //
            localNav.navigateForResult<Boolean>(AppRoutes.Wallet.dynamic("fromGame" to true)) {
                if (it == true) {
                    Log.e("Game", "从充值界面回来，调用 updateCoin 开始刷新")
                    //充值成功 调用h5 更新方法
                    webView.evaluateJavascript("javascript:updateCoin()") { result ->
                        Log.e("Game", " updateCoin 的结果是什么 如果有的话  ${result}")
                    }
                } else {
                    Log.e("Game", "从充值界面回来，充值失败")
                    //失败
                }
            }
        }
    }
}