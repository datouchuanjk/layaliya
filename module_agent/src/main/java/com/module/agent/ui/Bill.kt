package com.module.agent.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.agent.R
import com.module.agent.viewmodel.*
import com.module.basic.ui.paging.AppPagingBox
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.viewmodel.*

@Composable
internal fun Bill(viewModel: BillViewModel = apiHandlerViewModel()) {
    val pagingData = viewModel.pagingData
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .padding(horizontal = 15.dp)
    ) {
        Title()
        AppPagingBox(
            modifier = Modifier
                .fillMaxSize(),
            pagingData = pagingData
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(pagingData = pagingData, key = { it.uid }) { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .background(
                                color = Color.White,
                                shape = if (index == pagingData.lastIndex) RoundedCornerShape(
                                    bottomEnd = 8.dp,
                                    bottomStart = 8.dp
                                ) else RectangleShape
                            )
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "1982424",
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .wrapContentSize()
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = "张兴道",
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .wrapContentSize()
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = "我是傻逼",
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .wrapContentSize()
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = "500000",
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .wrapContentSize()
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = "500000",
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
    }
}


@Composable
private fun Title() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                color = Color(0xfff0f0f0),
                shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)
            )
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.agent_cycle),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.agent_idol_income),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.agent_proxy_commission_ratio),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.agent_proxy_commission),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.agent_gross_salary),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
    }
}