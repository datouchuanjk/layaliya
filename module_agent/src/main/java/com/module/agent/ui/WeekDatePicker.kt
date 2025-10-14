package com.module.agent.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
internal fun WeekDatePicker() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .height(44.dp)
            .width(180.dp)
            .background(color = Color.White, shape = RoundedCornerShape(22.dp))
            .wrapContentSize()
    ) {
        Text(text ="xxx", fontSize = 16.sp, color = Color.Black)
        Box(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(1.dp)
                .width(10.dp)
                .background(color = Color.Black)
        )
        Text(text = "xxx", fontSize = 16.sp, color = Color.Black)
    }
}