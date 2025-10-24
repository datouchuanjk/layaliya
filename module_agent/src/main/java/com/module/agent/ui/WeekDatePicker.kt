package com.module.agent.ui

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.develop.util.YMD
import com.module.basic.ui.picker.AppDatePicker
import com.module.basic.ui.AppDialog
import com.module.basic.util.onClick
import java.util.Calendar


@Composable
internal fun WeekDatePicker(
    onRefresh: (
        startTime: String,
        endTime: String,
    ) -> Unit
) {
    var isShow by remember {
        mutableStateOf(false)
    }

    var endTime by remember {
        mutableStateOf(Calendar.getInstance())
    }
    var startTime by remember {
        mutableStateOf(Calendar.getInstance().apply {
            timeInMillis = endTime.timeInMillis - 7 * 24 * 60 * 60 * 1000
        })
    }
    LaunchedEffect(Unit) {
        onRefresh(startTime.YMD,endTime.YMD)
    }
    if (isShow) {
        AppDialog(
            usePlatformDefaultWidth = false,
            layoutParamsSetting = {
                it.dimAmount = 0.1f
                it.gravity = Gravity.BOTTOM
            }, onDismissRequest = { isShow = false }
        ) {
            AppDatePicker(
                modifier = Modifier.background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                )
            ) {
                isShow = false
                startTime = it
                endTime = Calendar.getInstance().apply {
                    timeInMillis = it.timeInMillis + 7 * 24 * 60 * 60 * 1000L
                }
                onRefresh(startTime.YMD,endTime.YMD)
            }
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .height(44.dp)
            .background(color = Color.White, shape = RoundedCornerShape(22.dp))
            .padding(horizontal = 8.dp)
            .onClick {
                isShow = true
            }
    ) {
        Text(text = startTime.YMD, fontSize = 16.sp, color = Color.Black)
        Box(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(1.dp)
                .width(10.dp)
                .background(color = Color.Black)
        )
        Text(text = endTime.YMD, fontSize = 16.sp, color = Color.Black)
    }
}