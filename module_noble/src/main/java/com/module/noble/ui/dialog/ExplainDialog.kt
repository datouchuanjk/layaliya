package com.module.noble.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.module.basic.ui.AppDialog
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.noble.R

@Composable
internal fun ExplainDialog(
    isShow: Boolean,
    onDismissRequest: () -> Unit,
) {
    if (isShow) {
        AppDialog(
            onDismissRequest = onDismissRequest,
            usePlatformDefaultWidth = false,
            layoutParamsSetting = {
                it.dimAmount = 0f
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .background(color = Color.Black, shape = RoundedCornerShape(20.dp))
                    .border(1.dp, color = Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 13.dp)
            ) {
                Text(
                    text = stringResource(R.string.noble_explain),
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .wrapContentWidth()
                )
                Text(
                    text = stringResource(R.string.noble_explain_info1),
                    fontSize = 16.sp,
                    color = Color.White, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
                Text(
                    text = stringResource(R.string.noble_explain_info2),
                    fontSize = 16.sp,
                    color = Color.White, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
                Text(
                    text = stringResource(R.string.noble_explain_info3),
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 22.dp)
                        .fillMaxWidth()
                        .border(1.dp, color = Color.White.copy(0.2f), shape = RoundedCornerShape(20.dp))
                        .background( color = Color.White.copy(0.1f), shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 13.dp)
                        .padding(top = 12.dp)
                        .padding(bottom = 14.dp)
                ) {
                    Item(
                        title = "Buy Noble（100% Return",
                        days = stringArrayResource(R.array.vip_explain_days),
                        daysReturn = stringArrayResource(R.array.vip_explain_days_return1),
                    )
                    SpacerHeight(7.dp)
                    Item(
                        title = "Gift Noble（80% Return）",
                        days = stringArrayResource(R.array.vip_explain_days),
                        daysReturn = stringArrayResource(R.array.vip_explain_days_return2),
                    )
                }
                ///4
                Text(
                    text = stringResource(R.string.noble_explain_info4),
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 22.dp)
                        .fillMaxWidth()
                        .border(1.dp, color = Color.White.copy(0.2f), shape = RoundedCornerShape(20.dp))
                        .background( color = Color.White.copy(0.1f), shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 13.dp)
                        .padding(top = 12.dp)
                        .padding(bottom = 14.dp)
                ) {
                    Text(
                        text = "Purchase Noble rules",
                        fontSize = 16.sp,
                        color = Color(0xffFDD4AF),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                    SpacerHeight(2.dp)
                    Text(
                        "When your current noble level is still active, you can only extend the validity of your current level. Upgrading or changing to another level is not available until your current one expires.",
                        fontSize = 14.sp,
                        color = Color(0xffcccccc)
                    )
                    SpacerHeight(12.dp)
                    Text(
                        text = "Gift Noble rules",
                        fontSize = 16.sp,
                        color = Color(0xffFDD4AF),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                    SpacerHeight(2.dp)
                    Text(
                        "If the recipient already has an active noble level, you can only extend the validity of their current level. Upgrading or changing to another level is not available until their current one expires.",
                        fontSize = 14.sp,
                        color = Color(0xffcccccc)
                    )
                }
            }
        }
    }
}

@Composable
private fun Item(
    title: String,
    days: Array<String>,
    daysReturn: Array<String>,
) {
    Text(
        text = title,
        fontSize = 16.sp,
        color = Color(0xffFDD4AF),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.map {
            Text(text = it, fontSize = 12.sp, color = Color(0xffe6e6e6))
        }
    }
    SpacerHeight(4.dp)
    AppImage(
        R.drawable.noble_ic_day_point,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysReturn.mapIndexed { index, it ->
            Text(
                text = it,
                fontSize = 10.sp,
                color = Color(0xffcccccc),
                modifier = Modifier.padding(top = 5.dp - index.dp)
            )
        }
    }
}