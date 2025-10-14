package com.module.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.im.data.IMMessage

@Composable
internal fun LazyItemScope.UnknownMessageItem(item: IMMessage) {
    BasicMessageItem(item) {
        Text(
            text = item.text.orEmpty(),
            fontSize = 16.sp,
            color = if (item.isSelf) Color.White else Color(0xff333333),
            modifier = Modifier
                .background(
                    color = if (item.isSelf) Color(0xffFF5A96) else Color.White,
                    shape = if (item.isSelf) {
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 4.dp
                        )
                    } else {
                        RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 12.dp
                        )
                    }
                )
                .padding(vertical = 7.dp, horizontal = 12.dp)
        )
    }
}