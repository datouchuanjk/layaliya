package com.module.noble.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helper.develop.Background
import com.helper.develop.util.toast
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.AppTitleBar
import com.module.basic.ui.paging.AppPagingBox
import com.module.basic.ui.paging.items
import com.module.basic.util.onClick
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.noble.R
import com.module.noble.viewmodel.NobleHistoryViewModel

fun NavGraphBuilder.nobleHistoryScreen() = composable(route = AppRoutes.NobleHistory.static, arguments = AppRoutes.NobleHistory.arguments) {
    NobleHistoryScreen()
}

@Composable
internal fun NobleHistoryScreen(viewModel: NobleHistoryViewModel = apiHandlerViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        viewModel.receiveDiamondFlow
            .collect {
                context.toast(R.string.nobel_record_successful)
            }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(R.drawable.noble_bg),
                contentScale = ContentScale.FillBounds
            ),
        contentColor = Color.Transparent,
        containerColor = Color.Transparent,
        topBar = {
            AppTitleBar(
                backIconTint = Color.White,
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    color = Color.White
                ),
                showLine = false,
                text =viewModel.name
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 26.dp)
                        .fillMaxWidth()
                        .paint(
                            painterResource(if (viewModel.isNoble) R.drawable.noble_bg_already_noble else R.drawable.noble_bg_no_noble),
                            contentScale = ContentScale.Crop
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, start = 25.dp),
                        text = viewModel.name,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            brush = Brush.verticalGradient(
                                colors = if (viewModel.isNoble) {
                                    listOf(
                                        Color(0xffFFAC55),
                                        Color(0xff92350E)
                                    )
                                } else {
                                    listOf(
                                        Color(0xfff2f2ff),
                                        Color(0xff646289)
                                    )
                                }
                            )
                        )
                    )
                    SpacerHeight(41.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppImage(R.drawable.noble_ic_coin)
                        SpacerWidth(4.dp)
                        Row(modifier = Modifier.onClick {
                            viewModel.receiveDiamond()
                        }) {
                            Text(
                                text = "${viewModel.canReturnDiamondResponse?.diamond?:"0"}",
                                fontSize = 20.sp,
                                color = Color(0xffB15622)
                            )
                        }
                        SpacerWeight(1f)
                        Text(
                            stringResource(R.string.noble_claim),
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    color = Color(0xff333333),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(vertical = 2.dp, horizontal = 12.dp)
                        )
                    }
                }
                AppImage(
                    if (viewModel.isNoble) R.drawable.noble_ic_already_noble else R.drawable.noble_ic_no_noble,
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .size(120.dp)
                )
            }
            Text(
                text = stringResource(R.string.noble_historical_records),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .wrapContentSize()
            )
            Background(
                painter = painterResource(R.drawable.noble_bg_record_bottom), modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Background(
                        painter = painterResource(R.drawable.noble_bg_record_top)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            stringArrayResource(R.array.vip_historical_records_tabs).forEach { text ->
                                Text(
                                    text = text,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize()
                                )
                            }
                        }
                    }
                    List(viewModel)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.List(viewModel: NobleHistoryViewModel) {
    val pagingData = viewModel.pagingData
    AppPagingBox(
        skeletonContent = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(strokeWidth = 3.dp, color = Color.White)
            }
        },
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        pagingData = pagingData
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            items(pagingData = pagingData, key = { item ->
                item.id
            }, emptyContent = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillParentMaxSize()
                ) {
                    AppImage(R.drawable.noble_ic_no_record)
                    Text(
                        text = stringResource(R.string.noble_no_records_for_the_time_being),
                        fontSize = 16.sp,
                        color = Color(0xff999999)
                    )
                }
            }) { item ->
                HorizontalDivider(thickness = 1.dp, color = Color.White)
                Row(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.createTime.orEmpty(),
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize()
                    )
                    Text(
                        text = item.typeText.orEmpty(),
                        fontSize = 16.sp,
                        color =when(item.type){
                            1->Color(0xffFFB20B)
                            2->Color(0xffFF4070)
                            else ->Color.White
                        },
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize()
                    )
                    Text(
                        text = item.toUid.orEmpty(),
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize()
                    )
                    Text(
                        text = item.level.orEmpty(),
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize()
                    )
                }
            }
        }
    }
}