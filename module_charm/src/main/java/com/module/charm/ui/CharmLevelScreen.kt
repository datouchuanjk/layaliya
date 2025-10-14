package com.module.charm.ui

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
import com.module.charm.*
import com.module.charm.viewmodel.CharmLevelViewModel

fun NavGraphBuilder.charmLevelScreen() = composable(AppRoutes.CharmLevel.static) {
    CharmLevelScreen()
}

@Composable
internal fun CharmLevelScreen(viewModel: CharmLevelViewModel = apiHandlerViewModel()) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(R.drawable.charm_bg),
                contentScale = ContentScale.FillBounds
            ),
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        topBar = {
            AppTitleBar(
                showLine = false,
                backIconTint = Color.White,
                text = stringResource(R.string.charm_charm_level),
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
                text = stringResource(R.string.charm_level_rules),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .wrapContentWidth()
            )
            SpacerHeight(12.dp)
            Text(
                text = stringResource(R.string.charm_level_rules_detail),
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
private fun BgTop(viewModel: CharmLevelViewModel) {
    val charm by viewModel.charmLevelFlow.collectAsState()
    Background(
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.padding(horizontal = 8.dp),
        painter = rememberAsyncImagePainter(charm?.userInfo?.bg)
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
                    model = charm?.userInfo?.avatar,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
            Text(
                text = charm?.userInfo?.nickname.orEmpty(),
                fontSize = 14.sp,
                color = charm?.userInfo?.textColor ?: Color.White,
                modifier = Modifier.constrainAs(name) {
                    top.linkTo(parent.top)
                    start.linkTo(icon.end, 4.dp)
                })
            Text(
                text = "LV.${charm?.userInfo?.level}",
                fontSize = 20.sp,
                color = charm?.userInfo?.textColor ?: Color.White,
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
                    text = "LV${charm?.userInfo?.level}",
                    fontSize = 16.sp,
                    color = charm?.userInfo?.textColor ?: Color.White
                )
                SpacerWeight(1f)
                Text(
                    text = "LV${(charm?.userInfo?.level ?: 0) + 1}",
                    fontSize = 16.sp,
                    color = charm?.userInfo?.textColor ?: Color.White
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
                    color = Color.White,
                    topLeft = Offset(0f, 0f),
                    cornerRadius = CornerRadius(size.height / 2)
                )
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = charm?.userInfo?.progressColors?.toList() ?: listOf(
                            Color.White,
                            Color.White
                        )
                    ), topLeft = Offset(0f, 0f),
                    size = size.copy(
                        width = (charm?.userInfo?.progress ?: 0f) * size.width
                    ),
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
                text = "Need ${(charm?.userInfo?.nextLevelExp ?: 0) - (charm?.userInfo?.exp ?: 0)} experience to upgrade",
                color = charm?.userInfo?.textColor ?: Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun BottomInfo(viewModel: CharmLevelViewModel) {
    val charm by viewModel.charmLevelFlow.collectAsState()
    Background(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .padding(vertical = 12.dp),
        painter = painterResource(R.drawable.charm_bg_content),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            charm?.rewardInfo?.forEach {
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
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth()
                    ) {
                        Background(
                            modifier = Modifier.padding(start = 10.dp),
                            painter =  rememberAsyncImagePainter(it?.bg)
                        ) {
                            Text(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(27.dp)
                                    .padding(start = 14.dp)
                                    .wrapContentSize(),
                                text = it?.displayLevel.orEmpty(),
                                fontSize = 16.sp,
                                color = Color.White,
                            )
                        }
                        AppImage(
                            modifier = Modifier.size(36.dp),
                            model = it?.icon,
                        )
                    }
                }
            }
        }
    }
}