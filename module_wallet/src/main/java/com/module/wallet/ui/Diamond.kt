package com.module.wallet.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.helper.develop.Background
import com.module.wallet.R
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.paging.items
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.util.*
import com.module.basic.viewmodel.*
import com.module.wallet.viewmodel.*

@Composable
internal fun Diamond(viewModel: DiamondViewModel = apiHandlerViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            columns = GridCells.Fixed(3)
        ) {
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                Box(modifier = Modifier) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .padding(horizontal = 30.dp)
                            .background(
                                color = Color(0xffFFD0E8),
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                            .height(24.dp)
                            .padding(horizontal = 15.dp)
                            .background(
                                color = Color(0xffFFACD7),
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                    Background(
                        painter = painterResource(R.drawable.wallet_bg_diamond),
                        modifier = Modifier
                            .fillMaxWidth()
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
                                text = "UID:${viewModel.userInfo?.uuid.orEmpty()}",
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
                                    text = viewModel.userInfo?.diamond.orEmpty(),
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
            val pagingData = viewModel.pagingData
            itemsIndexed(pagingData = pagingData, key = {
                it.id.toString()
            }) { index, it ->
                Box(modifier = Modifier) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(top = 11.dp, start = 1.dp)
                            .fillMaxWidth()
                            .aspectRatio(107f / 76)
                            .border(
                                2.dp,
                                color = if (it.isSelected) Color(0xffff4070) else Color(0xffE6E6E6),
                                shape = RoundedCornerShape(12.dp)
                            ).onClick {
                                viewModel.selected(it)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AppImage(R.drawable.wallet_ic_coin)
                            SpacerWidth(4.dp)
                            Text(
                                it.num.toString(),
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Black
                            )
                        }
                        SpacerHeight(4.dp)
                        Text(
                            text = it.price.toString(),
                            fontSize = 16.sp,
                            color = Color(0xff999999)
                        )
                    }
                    if (it.isTop) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth(66f / 107)
                                .height(22.dp)
                                .appBrushBackground(
                                    shape = RoundedCornerShape(
                                        topStart = 11.dp,
                                        topEnd = 11.dp,
                                        bottomEnd = 11.dp
                                    )
                                )
                        ) {
                            Text(text = "${it.topValue}%", fontSize = 12.sp, color = Color.White)
                        }
                    }
                    if (it.isSelected) {
                        AppImage(
                            R.drawable.wallet_ic_my_checked,
                            modifier = Modifier.align(alignment = Alignment.BottomEnd)
                        )
                    }
                }
            }
        }
        val localActivity = LocalActivity.current
        Text(
            text = stringResource(R.string.wallet_confirm_recharge),
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .appBrushBackground(
                    shape = RoundedCornerShape(24.dp)
                )
                .onClick {
                    localActivity?:return@onClick
                    viewModel.buy(localActivity)
                }. padding (vertical = 10.dp)
                .wrapContentSize()
        )
    }
}