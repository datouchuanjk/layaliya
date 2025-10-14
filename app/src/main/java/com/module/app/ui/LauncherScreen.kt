package com.module.app.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.helper.develop.nav.*
import com.module.app.viewmodel.LauncherViewModel
import com.module.basic.route.*
import com.module.basic.viewmodel.*

fun NavGraphBuilder.launcherScreen() = composable(route = AppRoutes.Launcher.static) {
    LauncherScreen()
}

@Composable
fun LauncherScreen(viewModel: LauncherViewModel = apiHandlerViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(com.module.app.R.drawable.app_ic_round_launcher),
            contentDescription = null
        )
    }
    val localNav = LocalNavController.current
    LaunchedEffect(Unit) {
        viewModel.routeFlow
            .collect{
                localNav.navigateTo(it)
            }
    }
    LaunchedEffect(Unit) {
        viewModel.check()
    }
}