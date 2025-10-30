package com.module.chat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.helper.im.data.IMMessage
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerWidth
import com.module.chat.R

@Composable
internal fun LazyItemScope.BasicMessageItem(item: IMMessage, content: @Composable () -> Unit) {
    if (item.isSelf) {
        Self(item, content)
    } else {
        Other(item, content)
    }
}

@Composable
private fun LazyItemScope.Other(item: IMMessage, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .padding(15.dp)
    ) {
        IMAvatar(
            item.senderAvatar, modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
                .padding(end = 36.dp)
        ) {
            content()
            SpacerWidth(8.dp)
            ChatState(item.sendingState, item.isRead)
        }
    }
}

@Composable
private fun LazyItemScope.Self(item: IMMessage, content: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillParentMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
                .padding(start = 36.dp)
        ) {
            ChatState(item.sendingState, item.isRead)
            SpacerWidth(8.dp)
            content()
        }
        IMAvatar(
            item.senderAvatar, modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
private fun ChatState(sendingState: Int, isRead: Boolean) {
    Box(modifier = Modifier.size(13.dp), contentAlignment = Alignment.BottomEnd) {
        when (sendingState) {
            0 -> {}
            1 -> {
//                if (isRead) {
//                    AppImage(
//                        model = R.drawable.chat_ic_read,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                }
            }

            2 -> {
                AppImage(
                    model = R.drawable.chat_ic_send_error,
                    modifier = Modifier.fillMaxSize()
                )
            }

            3 -> {
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}