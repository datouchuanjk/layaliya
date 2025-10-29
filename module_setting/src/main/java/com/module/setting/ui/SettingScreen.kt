package com.module.setting.ui

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.navigateAndPopAll
import com.helper.develop.util.toast
import com.helper.develop.util.versionName
import com.module.basic.route.AppRoutes
import com.module.basic.sp.AppGlobal
import com.module.basic.sp.clearToken
import com.module.basic.ui.AppMoreIcon
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWeight
import com.module.basic.ui.AppTitleBar
import com.module.basic.util.onClick
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.setting.*
import com.module.setting.viewmodel.SettingViewModel
import org.koin.androidx.compose.get

fun NavGraphBuilder.settingScreen() = composable(route = AppRoutes.Setting.static) {
    SettingScreen()
}

@Composable
internal fun SettingScreen(viewModel: SettingViewModel = apiHandlerViewModel()) {
    Scaffold(
        containerColor = Color(0xffF5F5F5),
        topBar = {
            AppTitleBar(
                showLine = false,
                text = stringResource(R.string.setting_settings)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                val localNav = LocalNavController.current
                Item(stringResource(R.string.setting_user_agreement)) { title ->
                    localNav.navigate(
                        AppRoutes.WebView.dynamic(
                            "title" to title, "url" to "https://www.baidu.com"
                        )
                    )
                }
                Item(stringResource(R.string.setting_privacy_policy)) { title ->
                    localNav.navigate(
                        AppRoutes.WebView.dynamic(
                            "title" to title, "url" to "https://www.baidu.com"
                        )
                    )
                }
                Item(stringResource(R.string.setting_about_us)) { title ->
                    localNav.navigate(
                        AppRoutes.WebView.dynamic(
                            "title" to title, "url" to "https://www.baidu.com"
                        )
                    )
                }
            }
            SpacerHeight(12.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                val context = LocalContext.current
                Item(stringResource(R.string.setting_clear_cache)) {
                    viewModel.clearCache()
                }
                Item(stringResource(R.string.setting_version)) {
                    context.toast(R.string.setting_version) {
                        context.versionName
                    }
                }
            }
            SpacerHeight(12.dp)
            LoginOut {
                AppGlobal.exit()
            }
            SpacerHeight(12.dp)
        }
    }
}

@Composable
private fun Item(name: String, block: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.onClick { block(name) }) {
        Text(text = name, fontSize = 20.sp, color = Color.Black)
        SpacerWeight(1f)
        AppMoreIcon()
    }
}

@Composable
private fun LoginOut(block: () -> Unit) {
    Text(
        text = stringResource(R.string.setting_login_out),
        fontSize = 24.sp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xffFF76BD),
                        Color(0xffFF4070),
                    )
                ), shape = RoundedCornerShape(40.dp)
            )
            .onClick(onClick = block)
            .padding(vertical = 9.dp)
            .wrapContentSize()
    )
}