package com.module.wallet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.helper.develop.Background
import com.module.agent.R
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerWidth
import com.module.basic.util.todoImageUrl
import com.module.basic.viewmodel.*
import com.module.wallet.viewmodel.*


@Composable
internal fun Coins(viewModel: CoinsViewModel = apiHandlerViewModel()) {
    Box(modifier = Modifier.padding(top = 20.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .padding(horizontal = 45.dp)
                .background(color = Color(0xffFEF1C5), shape = RoundedCornerShape(12.dp))
        )
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .height(24.dp)
                .padding(horizontal = 30.dp)
                .background(color = Color(0xffFFE682), shape = RoundedCornerShape(12.dp))
        )
        Background(
            painter = painterResource(R.drawable.wallet_bg_coins),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(top = 24.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 15.dp)
            ) {
                val (icon, name, uid, coin, msg) = createRefs()
                AppImage(
                    model = todoImageUrl(), modifier = Modifier
                        .constrainAs(icon) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = viewModel.userInfo?.nickname.orEmpty(),
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.constrainAs(name) {
                        top.linkTo(icon.top)
                        start.linkTo(icon.end, 12.dp)
                    })
                Text(
                    text = "UID:${ viewModel.userInfo?.uuid.orEmpty()}",
                    fontSize = 16.sp,
                    color = Color(0xffe6e6e6),
                    modifier = Modifier.constrainAs(uid) {
                        top.linkTo(name.bottom, 3.dp)
                        start.linkTo(name.start)
                    })
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.constrainAs(coin) {
                        top.linkTo(icon.bottom, 20.dp)
                        start.linkTo(icon.start)
                    }) {
                    Text(
                        text = viewModel.userInfo?.coins.orEmpty(),
                        fontSize = 28.sp,
                        color = Color.White
                    )
                    SpacerWidth(8.dp)
                    AppImage(R.drawable.wallet_ic_coin)
                }
                Text(
                    text = stringResource(R.string.wallet_balance),
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.constrainAs(msg) {
                        top.linkTo(coin.bottom, 4.dp)
                        start.linkTo(icon.start)
                        bottom.linkTo(parent.bottom, 6.dp)
                        height = Dimension.wrapContent
                    })
            }
        }
    }
}