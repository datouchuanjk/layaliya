package com.module.app.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helper.develop.nav.*
import com.helper.im.IMHelper
import com.module.app.R
import com.module.basic.route.AppRoutes
import com.module.basic.util.onClick
import com.module.chat.ui.Home2
import com.module.community.ui.Home3
import com.module.mine.ui.Home4
import com.module.room.ui.Home1

fun NavGraphBuilder.mainScreen() = composable(route = AppRoutes.Main.static) {
    MainScreen()
}

@Composable
fun MainScreen() {
    val items = listOf(
        painterResource(R.drawable.app_ic_bottom_bar_room_unselected) to painterResource(R.drawable.app_ic_bottom_bar_room_selected),
        painterResource(R.drawable.app_ic_bottom_bar_chat_unselected) to painterResource(R.drawable.app_ic_bottom_bar_chat_selected),
        painterResource(R.drawable.app_ic_bottom_bar_add) to painterResource(R.drawable.app_ic_bottom_bar_add),
        painterResource(R.drawable.app_ic_bottom_bar_community_unselected) to painterResource(R.drawable.app_ic_bottom_bar_community_selected),
        painterResource(R.drawable.app_ic_bottom_bar_mine_unselected) to painterResource(R.drawable.app_ic_bottom_bar_mine_selected),
    )
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    Scaffold(
        bottomBar = {
            val navHostController = LocalNavController.current
            val totalUnreadCountChanged by IMHelper.conversationHandler.totalUnreadCountChanged.collectAsState()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = Color(0xff333333))
                    .padding(vertical = 9.dp)
            ) {
                repeat(items.count()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box {
                            Image(
                                modifier = Modifier
                                    .size(32.dp)
                                    .onClick {
                                        if (it == 2) {
                                            navHostController.navigate(AppRoutes.RoomCreateCheck.static)
                                        } else {
                                            selectedIndex = it
                                        }
                                    },
                                painter = if (selectedIndex == it) items[it].second else items[it].first,
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                            )
                            if (it == 1) {
                                Box(
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopEnd)
                                        .alpha(if (totalUnreadCountChanged > 0) 1f else 0f)
                                        .size(17.dp)
                                        .clip(CircleShape)
                                        .background(color = Color(0xffF13D30)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        if (totalUnreadCountChanged > 99) "99+" else totalUnreadCountChanged.toString(),
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            val stateHolder = rememberSaveableStateHolder()
            when (selectedIndex) {
                0 -> stateHolder.SaveableStateProvider("room") { Home1() }
                1 -> stateHolder.SaveableStateProvider("chat") { Home2() } //IM
                3 -> stateHolder.SaveableStateProvider("community") { Home3() } // 社区
                4 -> stateHolder.SaveableStateProvider("mine") { Home4() } //我的
            }
        }
    }
}

