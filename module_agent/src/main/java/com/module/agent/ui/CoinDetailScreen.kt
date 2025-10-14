package com.module.agent.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.module.agent.R
import com.module.agent.viewmodel.CoinDetailViewModel
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppTitleBar
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWeight
import com.module.basic.ui.paging.AppPagingRefresh
import com.module.basic.ui.paging.items
import com.module.basic.viewmodel.apiHandlerViewModel


fun NavGraphBuilder.coinDetailScreen() = composable(route = AppRoutes.CoinDetail.static) {
    CoinDetailScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CoinDetailScreen(viewModel: CoinDetailViewModel = apiHandlerViewModel()) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(R.drawable.agent_bg),
                contentScale = ContentScale.FillBounds
            ),
        topBar = {
            AppTitleBar(
                showLine = false,
                text = stringResource(R.string.mine_details_of_coins),
                backIconTint = Color.Black
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SpacerHeight(16.dp)
            WeekDatePicker()
            val pagingData = viewModel.pagingData
            AppPagingRefresh(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 12.dp),
                pagingData = pagingData
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                ) {

                    items(pagingData = pagingData, key = null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = it.action.orEmpty(),
                                    fontSize = 16.sp,
                                    color = Color(0xff333333)
                                )
                                Text(
                                    text = it.time.orEmpty(),
                                    fontSize = 12.sp,
                                    color = Color(0xff999999)
                                )
                            }
                            SpacerWeight(1f)
                            Text(text = it.time.orEmpty(), fontSize = 24.sp, color = Color.Black)
                        }
                    }

                }
            }
        }
    }
}

