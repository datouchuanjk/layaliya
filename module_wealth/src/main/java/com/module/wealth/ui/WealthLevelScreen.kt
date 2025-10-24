package com.module.wealth.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.helper.develop.Background
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWeight
import com.module.basic.ui.AppTitleBar
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.wealth.*
import com.module.wealth.viewmodel.WealthLevelViewModel

fun NavGraphBuilder.wealthLevelScreen() = composable(AppRoutes.WealthLevel.static) {
    WealthLevelScreen()
}

@Composable
internal fun WealthLevelScreen(viewModel: WealthLevelViewModel = apiHandlerViewModel()) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(R.drawable.wealth_bg),
                contentScale = ContentScale.FillBounds
            ),
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        topBar = {
            AppTitleBar(
                showLine = false,
                backIconTint = Color.White,
                text = stringResource(R.string.wealth_wealth_level),
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            SpacerHeight(10.dp)
            BgTop(viewModel)
            SpacerHeight(12.dp)
            Text(
                text = stringResource(R.string.wealth_level_rules),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .wrapContentWidth()
            )
            SpacerHeight(12.dp)
            Text(
                text = stringResource(R.string.wealth_level_rules_detail),
                fontSize = 16.sp,
                color = Color.White.copy(0.4f),
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .wrapContentWidth()
            )
            BottomInfo(viewModel)
        }
    }
}

@Composable
private fun BgTop(viewModel: WealthLevelViewModel) {
    val wealth by viewModel.wealthLevelFlow.collectAsState()
    Background(
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.padding(horizontal = 8.dp),
        painter = rememberAsyncImagePainter(wealth?.userInfo?.bg)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(top = 26.dp, bottom = 15.dp)
        ) {
            val (icon, name, level, fromTo, progress, message) = createRefs()
            Box(
                modifier = Modifier
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, 4.dp)
                    }
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = Color.White)
                    .padding(2.dp)
            ) {
                AppImage(
                    model = wealth?.userInfo?.avatar,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
            Text(
                text = wealth?.userInfo?.nickname.orEmpty(),
                fontSize = 14.sp,
                color = wealth?.userInfo?.textColor ?: Color.White,
                modifier = Modifier.constrainAs(name) {
                    top.linkTo(parent.top)
                    start.linkTo(icon.end, 4.dp)
                })
            Text(
                text = "LV.${wealth?.userInfo?.level}",
                fontSize = 20.sp,
                color = wealth?.userInfo?.textColor ?: Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(level) {
                    top.linkTo(name.bottom, 2.dp)
                    start.linkTo(name.start)
                })
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(fromTo) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    top.linkTo(icon.bottom, 53.dp)
                }
            ) {
                Text(
                    text = "LV${wealth?.userInfo?.level}",
                    fontSize = 16.sp,
                    color = wealth?.userInfo?.textColor ?: Color.White
                )
                SpacerWeight(1f)
                Text(
                    text = "LV${(wealth?.userInfo?.level ?: 0) + 1}",
                    fontSize = 16.sp,
                    color = wealth?.userInfo?.textColor ?: Color.White
                )
            }

            Canvas(modifier = Modifier.constrainAs(progress) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.value(8.dp)
                top.linkTo(fromTo.bottom, 5.dp)
            }) {
                drawCircle(color = Color.Red)
                drawRoundRect(
                    color = Color.White, topLeft = Offset(0f, 0f),
                    cornerRadius = CornerRadius(size.height / 2)
                )
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = wealth?.userInfo?.progressColors?.toList() ?: listOf(
                            Color.White,
                            Color.White
                        )
                    ), topLeft = Offset(0f, 0f),
                    size = size.copy(width = (wealth?.userInfo?.progress ?: 0f) * size.width),
                    cornerRadius = CornerRadius(size.height / 2)
                )
            }
            Text(
                modifier = Modifier.constrainAs(message) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    top.linkTo(progress.bottom, 5.dp)
                },
                text = "Need xxx experience to upgrade",
                color = wealth?.userInfo?.textColor ?: Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun BottomInfo(viewModel: WealthLevelViewModel) {
    val wealth by viewModel.wealthLevelFlow.collectAsState()
    Background(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .padding(vertical = 12.dp),
        painter = painterResource(R.drawable.wealth_bg_content),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            wealth?.rewardInfo?.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(),
                        text = it?.level.orEmpty(),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Background(
                        modifier = Modifier
                            .weight(2f)
                            .height(24.dp)
                            .padding(end = 22.dp)
                            .wrapContentWidth(),
                        painter = rememberAsyncImagePainter(it?.bg)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                        ) {
                            Background(
                                modifier = Modifier.padding(start = 5.dp),
                                painter = rememberAsyncImagePainter(it?.icon)
                            ) {
                                Text(
                                    text = it?.displayLevel.toString(),
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(38.dp)
                                        .padding(start = 15.dp)
                                        .wrapContentSize()
                                )
                            }
                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                text = "Zingme entered the roomroomroomroomroomroom",
                                fontSize = 12.sp,
                                color = Color.White,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}