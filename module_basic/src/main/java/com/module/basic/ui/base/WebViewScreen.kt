package com.module.basic.ui.base

import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppTitleBar

fun NavGraphBuilder.webViewScreen() =
    composable(route = AppRoutes.WebView.static, arguments = AppRoutes.WebView.arguments) {
        WebViewScreen()
    }

@Composable
internal fun WebViewScreen() {
    val title = (LocalLifecycleOwner.current as NavBackStackEntry).arguments?.getString("title").orEmpty()
    val url = (LocalLifecycleOwner.current as NavBackStackEntry).arguments?.getString("url").orEmpty()
    Scaffold(
        topBar = {
            AppTitleBar(text = title)
        }
    ) { innerPadding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            url: String?
                        ): Boolean {
                            return false
                        }
                    }
                    loadUrl(url)
                }
            }
        )
    }
}