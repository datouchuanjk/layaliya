package com.module.mine.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.module.basic.route.AppRoutes
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.AppTitleBar
import com.module.basic.ui.paging.AppPagingRefresh
import com.module.basic.ui.AppImage
import com.module.basic.ui.paging.items
import com.module.basic.util.onClick
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.mine.R
import com.module.mine.api.data.response.FollowOrFansResponse
import com.module.mine.viewmodel.FollowersOrFansViewModel

fun NavGraphBuilder.followersOrFansScreen() = composable(
    route = AppRoutes.FollowersOrFans.static,
    arguments = AppRoutes.FollowersOrFans.arguments
) {
    FollowersOrFansScreen()
}

/**
 * 关注or粉丝列表
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FollowersOrFansScreen(viewModel: FollowersOrFansViewModel = apiHandlerViewModel()) {
    Scaffold(
        topBar = {
            AppTitleBar(
                text = if (viewModel.type == 0) stringResource(R.string.mine_followers) else stringResource(
                    R.string.mine_fans
                )
            )
        }
    ) { innerPadding ->
        val pagingDate = viewModel.pagingDate
        AppPagingRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            pagingData = pagingDate
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(pagingData = pagingDate, key = { it.uid }) { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 15.dp)
                    ) {
                        AppImage(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            model = item.avatar
                        )
                        SpacerWidth(12.dp)
                        Column {
                            Text(
                                item.nickname.orEmpty(),
                                fontSize = 16.sp,
                                color = Color(0xff333333)
                            )
                            SpacerHeight(4.dp)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(
                                        color = Color(0xffC7D2DD),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(vertical = 3.dp, horizontal = 6.dp)
                            ) {
                                AppImage(R.drawable.mine_ic_white_star)
                                SpacerWidth(4.dp)
                                Text(
                                    "LV${item.nobleLevel}",
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                        }

                        SpacerWeight(1f)
                            when (item.status) {
                                1, 2 -> {
                                    Followed(item,viewModel)
                                }

                                0 -> {
                                    Unfollow(item,viewModel)
                                }
                            }

                    }
                }
            }
        }
    }
}

@Composable
private fun Unfollow(item: FollowOrFansResponse, viewModel: FollowersOrFansViewModel) {
    Box(
        modifier = Modifier
            .height(24.dp)
            .width(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color(0xFFFF4070))
            .onClick {
                viewModel.updateStatus(item)
            },
        contentAlignment = Alignment.Center
    ) {
        Text("+", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
    }
}

@Composable
private fun Followed(item: FollowOrFansResponse, viewModel: FollowersOrFansViewModel) {
    Box(
        modifier = Modifier
            .height(24.dp)
            .width(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color(0xffe6e6e6))
            .onClick {
                viewModel.updateStatus(item)
            },
        contentAlignment = Alignment.Center
    ) {
        Text("√", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xff999999))
    }
}