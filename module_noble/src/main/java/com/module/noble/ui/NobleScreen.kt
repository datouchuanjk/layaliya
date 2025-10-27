package com.module.noble.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.helper.develop.Background
import com.helper.develop.banner.Banner
import com.helper.develop.nav.LocalNavController
import com.helper.develop.util.padToMultiple
import com.helper.develop.util.toast
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppImage
import com.module.basic.ui.AppTabRow
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.basic.util.onClick
import com.module.noble.viewmodel.NobleViewModel
import com.module.noble.R
import com.module.noble.ui.dialog.ExplainDialog
import com.module.noble.ui.dialog.GiveToWhoDialog
import kotlinx.coroutines.launch

/**
 * 贵族界面
 */
fun NavGraphBuilder.nobleScreen() = composable(route = AppRoutes.Noble.static) {
    NobleScreen()
}

@Composable
internal fun NobleScreen(viewModel: NobleViewModel = apiHandlerViewModel()) {
    val bannerState = rememberPagerState {
        viewModel.nobleList.count()
    }
    LaunchedEffect(bannerState) {
        snapshotFlow { bannerState.settledPage }
            .collect {
                viewModel.selectedIndex(it)
            }
    }
    val scope = rememberCoroutineScope()
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
            AppTabRow(
                backIconTint = Color.White,
                withBackIcon = true,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 15.dp),
                selectedTextStyle = TextStyle(
                    fontSize = 20.sp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xffFFE9C5),
                            Color(0xffFFAE7C),
                        )
                    )
                ),
                unselectedTextStyle = TextStyle(
                    fontSize = 20.sp,
                    color = Color.White
                ),
                indicatorBackground = Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xffFFE9C5),
                            Color(0xffFFAE7C),
                        )
                    ), shape = RoundedCornerShape(3.dp)
                ),
                space = 40.dp,
                contentPadding = PaddingValues(start = 15.dp, end = 15.dp),
                tabs = stringArrayResource(R.array.noble_tabs),
                selectedIndex = bannerState.currentPage,
                onIndexChanged = {
                    scope.launch {
                        bannerState.animateScrollToPage(it)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SpacerHeight(30.dp)
            VipBanner(
                state = bannerState,
                viewModel = viewModel,
            )
            SpacerHeight(30.dp)
            Background(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 15.dp),
                painter = rememberAsyncImagePainter(viewModel.selectedNobleResponse?.bg)
            ) {
                Box {
                    val scrollState = rememberScrollState()
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp, vertical = 24.dp)
                            .verticalScroll(scrollState),
                        maxItemsInEachRow = 3,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        viewModel.selectedNobelPowerDataList.padToMultiple(3).forEach { item ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1 / 3f)
                            ) {
                                if (item != null) {
                                    AppImage(
                                        model = item.icon,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .paint(
                                                painter = rememberAsyncImagePainter(viewModel.selectedNobleResponse?.powerBg),
                                                contentScale = ContentScale.Crop
                                            )
                                            .wrapContentSize()
                                            .size(30.dp),
                                    )
                                    SpacerHeight(4.dp)
                                    Text(
                                        minLines = 2,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        text = item.name.orEmpty(),
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    Canvas(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .padding(vertical = 5.dp)
                            .padding(end = 1.5.dp)
                            .width(1.dp)
                    ) {
                        val totalHeight = size.height
                        val visibleRatio = totalHeight / (scrollState.maxValue + totalHeight)
                        val scrollbarHeight = totalHeight * visibleRatio
                        val scrollbarOffset =
                            (scrollState.value.toFloat() / scrollState.maxValue) * (totalHeight - scrollbarHeight)

                        drawRoundRect(
                            color = Color.Gray.copy(alpha = 0.6f),
                            topLeft = Offset(0f, scrollbarOffset),
                            size = Size(size.width, scrollbarHeight),
                            cornerRadius = CornerRadius(8.dp.toPx())
                        )
                    }
                }

            }
            SpacerHeight(28.dp)
            BottomButton(viewModel)
        }
    }
}


@Composable
private fun BottomButton(viewModel: NobleViewModel) {
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        viewModel.buyFlow.collect {
            context.toast(R.string.noble_buy_successful)
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.giveFlow.collect {
            context.toast(R.string.noble_give_successful)
        }
    }
    HorizontalDivider(thickness = 0.1.dp, color = Color.White)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
            .padding(start = 23.dp, end = 15.dp)
    ) {
        AppImage(R.drawable.noble_ic_coin)
        SpacerWidth(4.dp)
        Text(
            text = viewModel.selectedNobleResponse?.price?.toString().orEmpty(),
            color = Color(0xffFFE9C5),
            fontSize = 20.sp
        )
        Text(text = "/30 day ", color = Color(0xffFFAE7C), fontSize = 12.sp)
        SpacerWidth(2.dp)

        var isShow by remember {
            mutableStateOf(false)
        }
//        AppImage(R.drawable.noble_ic_why)
        GiveToWhoDialog(
            isShow = isShow,
            onDismissRequest = { isShow = false },
            onConfirm = {
                viewModel.give(it)
            }
        )
        SpacerWeight(1f)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .height(28.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xffFFE9C5),
                            Color(0xffFFAE7C),
                        )
                    ), shape = RoundedCornerShape(20.dp)
                )
        ) {
            Text(
                stringResource(R.string.noble_give),
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(1.dp)
                    .background(
                        color = Color(0xff252422), shape = RoundedCornerShape(
                            topStart = 20.dp,
                            bottomStart = 20.dp
                        )
                    )
                    .onClick {
                        isShow = true
                    }
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight()
            )
            Text(
                stringResource(R.string.noble_buy), fontSize = 12.sp, color = Color(0xff333333),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp)
                    .onClick {
                        viewModel.buy()
                    }
                    .wrapContentHeight()
            )
        }
    }
}

@Composable
private fun VipBanner(
    state: PagerState,
    viewModel: NobleViewModel,
) {
    var isShowExplainDialog by remember {
        mutableStateOf(false)
    }
    ExplainDialog(
        isShow = isShowExplainDialog,
        onDismissRequest = {
            isShowExplainDialog = false
        }
    )
    Banner(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(314 / 141f),
        state = state,
        animator = null
    ) { page ->
        val item = viewModel.nobleList[page]
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Background(
                modifier = Modifier
                    .padding(top = 21.dp)
                    .fillMaxSize(),
                painter = rememberAsyncImagePainter(item.iconBg)
            ) {
                Box {
                    Column {
                        Text(
                            modifier = Modifier.padding(top = 12.dp, start = 9.dp),
                            text = item.name.orEmpty(),
                            style = TextStyle(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                brush = item.textColor
                            )
                        )
                        if (viewModel.nobleExpireTime != null) {
                            Text(
                                modifier = Modifier.padding(start = 14.dp),
                                text = "Expiration Date: ${viewModel.nobleExpireTime}",
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    color = item.secondTextColor
                                )
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomStart)
                            .onClick {
                                isShowExplainDialog = true
                            }
                            .padding(bottom = 22.dp, start = 14.dp),
                    ) {
                        Text(
                            text = item.description.orEmpty(),
                            style = TextStyle(
                                fontSize = 10.sp,
                                color = item.secondTextColor
                            )
                        )
                        SpacerWidth(3.dp)
                        AppImage(
                            model = R.drawable.noble_ic_why
                        )
                    }
                }
            }
            val localNav = LocalNavController.current
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
            ) {
                AppImage(
                    modifier = Modifier.size(120.dp),
                    model = item.icon,
                )
                SpacerHeight(4.dp)
                Row(
                    modifier = Modifier
                        .background(
                            color = Color(0xff333333),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .onClick {
                            localNav.navigate(AppRoutes.NobleHistory.dynamic("name" to viewModel.selectedNobleResponse?.name.orEmpty()))
                        }
                        .padding(horizontal = 13.dp, vertical = 6.dp)
                ) {

                    Text("Get back", fontSize = 12.sp, color = Color.White)
                    SpacerWidth(2.dp)
                    AppImage(
                        model = R.drawable.noble_ic_coin
                    )
                }
            }
        }
    }
}