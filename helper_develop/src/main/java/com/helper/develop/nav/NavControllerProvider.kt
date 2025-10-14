package com.helper.develop.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("NavController not provided")
}

@Composable
fun NavControllerLocalProvider(
    navController: NavHostController = rememberNavController(),
    content: @Composable (NavHostController) -> Unit
) {
    CompositionLocalProvider(LocalNavController provides navController) {
        content(navController)
    }
}

