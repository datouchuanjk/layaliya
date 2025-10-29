package com.module.basic.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppImage
import com.module.basic.ui.AppTitleBar

fun NavGraphBuilder.bigImageScreen() =
    composable(route = AppRoutes.BigImage.static, arguments = AppRoutes.BigImage.arguments) {
        val images = it.arguments?.getString("images")?.split(",").orEmpty()
        val index = it.arguments?.getInt("index")?:0
        BigImageScreen(index,images)
    }

@Composable
internal fun BigImageScreen(index:Int,images: List<String>) {

    Scaffold(containerColor = Color.Black, topBar = {
        AppTitleBar(
            text = "",
            showLine = false,
            backIconTint = Color.White
        )
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                state = rememberPagerState(index) {
                    images.count()
                }
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ){
                    AppImage(
                        model = images[it]
                    )
                }
            }
        }
    }
}


