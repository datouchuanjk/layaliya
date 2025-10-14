package com.module.agent.ui

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helper.develop.nav.LocalNavController
import com.helper.develop.util.toast
import com.module.agent.R
import com.module.agent.viewmodel.CoinMerchantViewModel
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppImage
import com.module.basic.ui.AppTitleBar
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.util.*
import com.module.basic.viewmodel.apiHandlerViewModel

fun NavGraphBuilder.coinMerchantScreen() = composable(route = AppRoutes.CoinMerchant.static) {
    CoinMerchantScreen()
}

/**
 * 所谓B商?
 */
@Composable
internal fun CoinMerchantScreen(viewModel: CoinMerchantViewModel = apiHandlerViewModel()) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(R.drawable.agent_bg),
                contentScale = ContentScale.FillBounds
            ), topBar = {
            AppTitleBar(
                backIconTint = Color.Black,
                text = stringResource(R.string.mine_coin_merchant)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            UserInfo(viewModel)
            OtherUserInfo(viewModel)
        }
    }
}

@Composable
private fun UserInfo(viewModel: CoinMerchantViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        val (icon, name, uid, createTime, coin) = createRefs()

        AppImage(
            model = viewModel.userResponse?.avatar,
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(parent.top, 17.dp)
                    start.linkTo(parent.start, 12.dp)
                }
                .size(60.dp)
                .clip(CircleShape)
        )
        Text(
            text = viewModel.userResponse?.nickname.orEmpty(),
            fontSize = 16.sp,
            color = Color(0xff333333),
            modifier = Modifier.constrainAs(name) {
                top.linkTo(icon.top)
                start.linkTo(icon.end, 14.dp)
            })
        Text(
            text = "UID:${viewModel.userResponse?.uuid.orEmpty()}",
            fontSize = 14.sp,
            color = Color(0xff999999),
            modifier = Modifier.constrainAs(uid) {
                top.linkTo(name.bottom, 3.dp)
                start.linkTo(name.start, 0.dp)
            })
        Text(
            text = "这里需要一个时间，但是目前没有",
            fontSize = 12.sp,
            color = Color(0xff999999),
            modifier = Modifier.constrainAs(createTime) {
                top.linkTo(uid.bottom, 3.dp)
                start.linkTo(uid.start, 0.dp)
            })
        Row(
            modifier = Modifier
                .constrainAs(coin) {
                    top.linkTo(icon.bottom, 13.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .background(color = Color(0xfff5f5f5), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.mine_remaining_currency),
                    fontSize = 14.sp,
                    color = Color(0xff999999)
                )
                SpacerHeight(6.dp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = viewModel.userResponse?.coins.orEmpty(),
                        fontSize = 24.sp,
                        color = Color(0xff333333)
                    )
                    AppImage(model = R.drawable.mine_ic_store_coin)
                }
                val localNav = LocalNavController.current
                Text(
                    modifier = Modifier.onClick{
                        localNav.navigate(AppRoutes.CoinDetail.static)
                    },
                    text = stringResource(R.string.mine_diamond_details),
                    fontSize = 10.sp,
                    color = Color(0xff999999)
                )
            }
            SpacerWidth(8.dp)
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .appBrushBackground(
                       shape = RoundedCornerShape(15.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                text = stringResource(R.string.mine_recharge),
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun OtherUserInfo(viewModel: CoinMerchantViewModel) {
    val local = LocalOnBackPressedDispatcherOwner.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .padding(top = 12.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        OtherUserInfoUID(viewModel)
        SpacerHeight(12.dp)
        OtherUserInfoNickname(viewModel)
        SpacerHeight(12.dp)
        OtherUserInfoGift(viewModel)
        SpacerHeight(12.dp)
        val context = LocalContext.current
        Row {
            LaunchedEffect(viewModel) {
                viewModel.confirmSuccessful.collect {
                    context.toast(R.string.mine_confirm_successful)
                }
            }
            Text(
                text = stringResource(R.string.mine_cancel),
                fontSize = 20.sp,
                color = Color(0xffcccccc),
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .border(
                        1.dp, color = Color(0xffcccccc), shape = RoundedCornerShape(20.dp)
                    )
                    .onClick {
                        local?.onBackPressedDispatcher?.onBackPressed()
                    }
                    .wrapContentSize()
            )
            SpacerWidth(13.dp)
            Text(
                text = stringResource(R.string.mine_confirm),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .appBrushBackground(
                        shape = RoundedCornerShape(20.dp)
                    )
                    .onClick {
                        viewModel.confirm()
                    }
                    .wrapContentSize()
            )
        }
    }
}

@Composable
private fun OtherUserInfoUID(viewModel: CoinMerchantViewModel) {
    Text(
        text = stringResource(R.string.mine_uid),
        fontSize = 20.sp,
        color = Color(0xff333333),
        fontWeight = FontWeight.Black
    )
    Row(
        modifier = Modifier
            .padding(top = 6.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .background(color = Color(0xfff5f5f5), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp)
                .wrapContentHeight(),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color(0xff333333)
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            value = viewModel.uid,
            onValueChange = viewModel::uid,
            decorationBox = {
                if (viewModel.uid.isEmpty()) {
                    Text(
                        text = stringResource(R.string.mine_please_enter_the_uid),
                        fontSize = 16.sp,
                        color = Color(0xffCCCCCC)
                    )
                }
                it()
            }
        )
        SpacerWidth(12.dp)
        Text(
            text = stringResource(R.string.mine_query),
            modifier = Modifier
                .background(
                    color = Color(0xff999999),
                    shape = RoundedCornerShape(15.dp)
                )
                .onClick {
                    viewModel.query()
                }
                .padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
private fun OtherUserInfoNickname(viewModel: CoinMerchantViewModel) {
    val userInfo = viewModel.userInfo
    Text(
        text = stringResource(R.string.mine_avatar_and_nickname),
        fontSize = 20.sp,
        color = Color(0xff333333),
        fontWeight = FontWeight.Black
    )
    Row(
        modifier = Modifier
            .padding(top = 6.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (userInfo?.avatar.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color = Color(0xfff5f5f5))
            )
        } else {
            AppImage(
                model = userInfo.avatar,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }
        SpacerWidth(12.dp)
        Text(
            text = userInfo?.nickname.orEmpty(),
            fontSize = 16.sp,
            color = Color(0xff333333),
            modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .background(color = Color(0xfff5f5f5), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp)
                .wrapContentHeight()
        )
    }
}

@Composable
private fun OtherUserInfoGift(viewModel: CoinMerchantViewModel) {
    Text(
        text = stringResource(R.string.mine_gift_diamonds),
        fontSize = 20.sp,
        color = Color(0xff333333),
        fontWeight = FontWeight.Black
    )

    BasicTextField(
        modifier = Modifier
            .padding(top = 6.dp)
            .fillMaxWidth()
            .height(37.dp)
            .background(color = Color(0xfff5f5f5), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp)
            .wrapContentHeight(),
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color(0xff333333)
        ),
        value = viewModel.num,
        onValueChange = viewModel::num,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        decorationBox = {
            if (viewModel.num.isEmpty()) {
                Text(
                    text = stringResource(R.string.mine_please_enter_the_number),
                    fontSize = 16.sp,
                    color = Color(0xffCCCCCC)
                )
            }
            it()
        }
    )
}

