package com.module.agent.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.module.agent.R
import com.module.agent.viewmodel.AdminViewModel
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppTitleBar
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.paging.items
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.util.*
import com.module.basic.viewmodel.apiHandlerViewModel

fun NavGraphBuilder.adminScreen() = composable(route = AppRoutes.Admin.static) {
    AdminScreen()
}

@Composable
internal fun AdminScreen(viewModel: AdminViewModel = apiHandlerViewModel()) {
    Scaffold(
        topBar = {
            AppTitleBar(text = stringResource(R.string.mine_admin))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(R.drawable.agent_bg),
                    contentScale = ContentScale.FillBounds
                )
                .padding(it)
        ) {
            SpacerHeight(16.dp)
            WeekDatePicker { startTime, endTime ->
                viewModel.refreshByTime(startTime, endTime)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(horizontal = 15.dp)
            ) {
                Top()
                List(viewModel)
            }

        }
    }
}

@Composable
private fun Top() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(
                color = Color(0xfff0f0f0),
                shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)
            )
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.mine_agent_nickname),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.mine_bound_idol),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.mine_total_coins),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.mine_agent_revenue),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
    }
}

@Composable
private fun List(viewModel: AdminViewModel) {
    val pagingData = viewModel.pagingData
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(pagingData = pagingData, key = { it.uid.toString() }) { index, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .background(
                        color = Color.White,
                        shape = if (index == pagingData.count - 1) RoundedCornerShape(
                            bottomEnd = 8.dp,
                            bottomStart = 8.dp
                        ) else RectangleShape
                    )
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = item.nickname.toString(),
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .wrapContentSize()
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = item.anchorNum.toString(),
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .wrapContentSize()
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = item.anchorIncoming.toString(),
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .wrapContentSize()
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = item.agentIncoming.toString(),
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .wrapContentSize()
                )
            }
        }
    }
}