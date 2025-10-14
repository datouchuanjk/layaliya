package com.module.mine.ui

import android.content.SharedPreferences
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.helper.develop.nav.LocalNavController
import com.module.basic.api.data.response.UserResponse
import com.module.basic.route.AppRoutes
import com.module.basic.sp.getHiddenIdentity
import com.module.basic.sp.putHiddenIdentity
import com.module.basic.ui.AppMoreIcon
import com.module.basic.ui.AppSwitch
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.basic.util.onClick
import com.module.mine.R
import com.module.mine.viewmodel.MineViewModel
import org.koin.androidx.compose.get
import kotlin.math.roundToInt

@Composable
fun Home4() {
    MineScreen()
}

private class MineAction(
    val name: String,
    val icon: Int,
    val onClick: ((NavHostController) -> Unit)? = null
)

private val mineActionIcons = listOf(
    R.drawable.mine_ic_action_store to { nav: NavHostController ->
        nav.navigate(AppRoutes.Store.static)
    },
    R.drawable.mine_ic_action_my_decoration to { nav: NavHostController ->
        nav.navigate(AppRoutes.Bag.static)
    },
    R.drawable.mine_ic_action_noble to { nav: NavHostController ->
        nav.navigate(AppRoutes.Noble.static)
    },
    R.drawable.mine_ic_action_agency to { nav: NavHostController ->
        nav.navigate(AppRoutes.Agent.static)
    },
    R.drawable.mine_ic_action_admin to { nav: NavHostController ->
        nav.navigate(AppRoutes.Admin.dynamic("isAdmin" to true))
    },
    R.drawable.mine_ic_action_bd to { nav: NavHostController ->
        nav.navigate(AppRoutes.BD.dynamic("isAdmin" to false))
    },
    R.drawable.mine_ic_action_bd to { nav: NavHostController ->
        nav.navigate(AppRoutes.CoinMerchant.static)
    },

)

@Composable
internal fun MineScreen(viewModel: MineViewModel = apiHandlerViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(R.drawable.mine_bg),
                contentScale = ContentScale.FillBounds
            )
            .statusBarsPadding()
            .padding(bottom = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        UserInfo(viewModel.userInfo)
        MyWallet()
        Actions()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(top = 12.dp)
        ) {
            CharmLevel(viewModel.userInfo)
            SpacerWidth(13.dp)
            WealthLevel(viewModel.userInfo)
        }
        HiddenIdentity()
        Setting()
    }
}

@Composable
private fun UserInfo(userInfo: UserResponse?) {
    val navHostController = LocalNavController.current
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .padding(top = 12.dp)
            .background(color = Color.White.copy(0.4f), shape = RoundedCornerShape(20.dp))
    ) {
        val (image, name, uid, info) = createRefs()
        Box(
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start, margin = 17.dp)
                    top.linkTo(parent.top, margin = 14.dp)
                }
                .size(65.dp)
                .clip(CircleShape)
                .background(color = Color.White)
                .padding(2.dp)
        ) {
            AppImage(
                model = userInfo?.avatar,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
            )
        }
        Row(
            modifier = Modifier
                .constrainAs(name) {
                    start.linkTo(image.end, margin = 8.dp)
                    top.linkTo(image.top)
                    end.linkTo(parent.end, margin = 12.dp)
                    width = Dimension.fillToConstraints
                }
                .onClick {
                    navHostController.navigate(
                        AppRoutes.PersonCenter.dynamic("uid" to userInfo?.id.toString())
                    )
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(userInfo?.nickname.orEmpty(), fontSize = 20.sp, color = Color(0xff333333))
            SpacerWidth(6.dp)
            SpacerWeight(1f)
            AppMoreIcon()
        }
        Text(
            text = "UID:${userInfo?.id.toString()}",
            fontSize = 13.sp,
            color = Color(0xff999999),
            modifier = Modifier
                .constrainAs(uid) {
                    start.linkTo(name.start)
                    end.linkTo(name.end)
                    top.linkTo(name.bottom, margin = 5.dp)
                    width = Dimension.fillToConstraints
                })

        Row(
            modifier = Modifier
                .constrainAs(info) {
                    top.linkTo(image.bottom, 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                    width = Dimension.fillToConstraints
                }) {
            val strings = listOf(
                userInfo?.followNum to stringResource(R.string.mine_followers),
                userInfo?.fansNum to stringResource(R.string.mine_fans),
                userInfo?.followRoomNum to stringResource(R.string.mine_rooms),
            )
            repeat(3) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .onClick {
                            when (it) {
                                0 -> navHostController.navigate(
                                    AppRoutes.FollowersOrFans.dynamic(
                                        "type" to 0,
                                        "uid" to userInfo?.id.toString()
                                    )
                                )

                                1 -> navHostController.navigate(
                                    AppRoutes.FollowersOrFans.dynamic(
                                        "type" to 1,
                                        "uid" to userInfo?.id.toString()
                                    )
                                )

                                2 -> {
                                    navHostController.navigate(AppRoutes.MyRoom.static)
                                }
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val item = strings[it]
                    Text(
                        text = item.first?.toString() ?: "0",
                        fontSize = 24.sp,
                        color = Color(0xff333333),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.second,
                        fontSize = 12.sp,
                        color = Color(0xff999999)
                    )
                }
            }
        }
    }
}

@Composable
private fun MyWallet() {
    val localNav = LocalNavController.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .padding(top = 12.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .onClick {
                localNav.navigate(AppRoutes.Wallet.static)
            }
            .padding(8.dp)
    ){
        SpacerWidth(15.dp)
        AppImage(model = R.drawable.mine_ic_wallet)
        SpacerWidth(5.dp)
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                stringResource(R.string.mine_wallet),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xff333333)
            )
            Text(
                stringResource(R.string.mine_view_more),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xff999999)
            )
        }
    }
}

@Composable
private fun Actions() {
    val localNav = LocalNavController.current
    val actions = stringArrayResource(R.array.mine_actions)
    val mineActions = remember {
        actions.mapIndexed { index, item ->
            val icon = mineActionIcons[index]
            MineAction(
                name = item,
                icon = icon.first,
                onClick = icon.second
            )
        }
    }
    FlowRow(
        maxItemsInEachRow = 4,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .padding(horizontal = 15.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp)
    ) {
        repeat(8) { index->
            val it =  mineActions.getOrNull(index)
            if(it==null){
                Box(modifier = Modifier.weight(1f))
            }else{
            Column(
                modifier = Modifier
                    .weight(1f)
                    .onClick {
                        it.onClick?.invoke(localNav)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    model = it.icon,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
                SpacerHeight(6.dp)
                Text(text = it.name, fontSize = 12.sp, color = Color.Black)
            }
        }
        }
    }
}

@Composable
private fun RowScope.CharmLevel(userInfo: UserResponse?) {
    val localNav = LocalNavController.current
    Row(
        modifier = Modifier
            .weight(1f)
            .height(66.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xffF2E0FF),
                        Color.White,
                    )
                ), shape = RoundedCornerShape(16.dp)
            )
            .onClick {
                localNav.navigate(AppRoutes.CharmLevel.static)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                stringResource(R.string.mine_charm_level),
                color = Color(0xffA937FE),
                fontSize = 16.sp
            )
            SpacerHeight(2.dp)
            Text(
                stringResource(R.string.mine_view_more),
                color = Color(0xff999999),
                fontSize = 12.sp
            )
        }
        SpacerWeight(1f)
        CircleProgress(
            color1 = Color(0xffF3E2FF),
            color2 = Color(0xffD65CFE),
            color3 = Color(0xffA937FE),
            color4 = Color(0xffF475FF),
            level = userInfo?.charmLevel ?: 0,
            progress = (userInfo?.charmExp ?: 0) / 100f
        )
    }
}

@Composable
private fun RowScope.WealthLevel(userInfo: UserResponse?) {
    val localNav = LocalNavController.current
    Row(
        modifier = Modifier
            .weight(1f)
            .height(66.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xffFFE7D6),
                        Color.White,
                    )
                ), shape = RoundedCornerShape(16.dp)
            )
            .onClick {
                localNav.navigate(AppRoutes.WealthLevel.static)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                stringResource(R.string.mine_wealth_level),
                color = Color(0xffFF8733),
                fontSize = 16.sp
            )
            SpacerHeight(2.dp)
            Text(
                stringResource(R.string.mine_view_more),
                color = Color(0xff999999),
                fontSize = 12.sp
            )
        }
        SpacerWeight(1f)
        CircleProgress(
            color1 = Color(0xffFFE7D6),
            color2 = Color(0xffFF9B3E),
            color3 = Color(0xffFF8669),
            color4 = Color(0xffFF4070),
            level = userInfo?.wealthLevel ?: 0,
            progress = (userInfo?.wealthExp ?: 0) / 100f
        )
    }
}

@Composable
private fun HiddenIdentity() {
    Row(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 15.dp)
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppImage(model = R.drawable.mine_ic_action_hidden_identity)
        SpacerWidth(16.dp)
        Text(
            text = stringResource(R.string.mine_hidden_identity),
            fontSize = 16.sp,
            color = Color.Black
        )
        val sp = get<SharedPreferences>()
        var b by remember {
            mutableStateOf(sp.getHiddenIdentity())
        }
        SpacerWeight(1f)
        AppSwitch(
            checked = b
        ) {
            b = it
            sp.putHiddenIdentity(it)
        }
    }
}

@Composable
private fun Setting() {
    val localNav = LocalNavController.current
    Row(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 15.dp)
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .onClick {
                localNav.navigate(AppRoutes.Setting.static)
            }
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppImage(model = R.drawable.mine_ic_action_setting)
        SpacerWidth(16.dp)
        Text(
            text = stringResource(R.string.mine_settings),
            fontSize = 16.sp,
            color = Color.Black
        )
        SpacerWeight(1f)
        AppMoreIcon()
    }
}


@Composable
private fun CircleProgress(
    progress: Float,
    level: Int,
    color1: Color,
    color2: Color,
    color3: Color,
    color4: Color
) {
    val density = LocalDensity.current
    val width = with(density) {
        10.dp.toPx()
    }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = color1, startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = Offset(width / 2, width / 2),
                size = androidx.compose.ui.geometry.Size(size.width - width, size.height - width),
                style = Stroke(
                    width = width,
                )
            )
            drawArc(
                color = color2, startAngle = 0f, sweepAngle = 360 * progress, useCenter = false,
                topLeft = Offset(width / 2, width / 2),
                size = androidx.compose.ui.geometry.Size(size.width - width, size.height - width),
                style = Stroke(
                    width = width,
                    cap = StrokeCap.Round
                )
            )
        }
        Text(
            text = "${(progress * 100).roundToInt()}%",
            fontSize = 12.sp,
            color = color3,
            modifier = Modifier.align(alignment = Alignment.Center)
        )
        Text(
            text = "Lv${level}",
            fontSize = 10.sp,
            color = Color.White,
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
                .height(18.dp)
                .background(color = color4, shape = RoundedCornerShape(9.dp))
                .wrapContentSize()
        )
    }
}