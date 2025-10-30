package com.module.chatroom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.develop.paging.PagingData
import com.helper.develop.util.fromJson
import com.helper.develop.util.getIntOrNull
import com.helper.develop.util.getStringOrNull
import com.helper.im.data.IMChatroomMessage
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.paging.items
import com.module.chatroom.R
import com.module.chatroom.api.data.response.ChatroomInfoResponse
import com.module.chatroom.viewmodel.ChatRoomViewModel
import org.json.JSONObject

@Composable
internal fun RowScope.ChatroomTextMessage(
    viewModel: ChatRoomViewModel,
    pagingData: PagingData<IMChatroomMessage>?
) {
    pagingData ?: return
    val state = rememberLazyListState()
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.totalItemsCount }
            .collect {
                state.animateScrollToItem(0)
            }
    }
    LazyColumn(
        state = state,
        reverseLayout = true,
        contentPadding = PaddingValues(vertical = 15.dp),
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            pagingData = pagingData,
            key = { it.messageId },
            emptyContent = null,
            loadingContent = null,
            loadErrorContent = null,
            notLoadingContent = null,
        ) {
            when (it.body) {
                null -> TextMessage(it)
                else -> CurrentMessage(it)
            }
        }
    }
}

@Composable
private fun TextMessage(item: IMChatroomMessage) {
    Row {
        if (item.senderIsMysteriousPerson) {
            AppImage(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color = Color(0xffD9D9D9)),
                model = R.drawable.room_ic_no_body_avatar
            )
        } else {
            AppImage(
                model = item.senderAvatar,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
        }

        SpacerWidth(11.dp)
        Column {
            Text(
                if (item.senderIsMysteriousPerson) stringResource(R.string.room_no_body_nickname) else item.senderName.orEmpty(),
                fontSize = 14.sp,
                color = Color.White
            )
            SpacerHeight(4.dp)

            Text(
                "${
                    if (item.receiverIsMysteriousPerson) {
                        stringResource(R.string.room_no_body_nickname)
                    } else {
                        item.receiverName
                    }.run {
                        if (this.isNullOrEmpty()) {
                            ""
                        } else {
                            "@ $this"
                        }
                    }
                } ${item.text.orEmpty()}",
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier
                    .background(
                        color = Color.White.copy(0.2f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            )
        }
    }
}

@Composable
private fun CurrentMessage(item: IMChatroomMessage) {
    val body = item.body ?: return
    when (body.code) {
        1003 -> {
            val message = body.data.fromJson<ChatroomInfoResponse.Notice>()?.msg ?: return
            Text(text = message, fontSize = 12.sp, color = Color.White)
        }

        1037 -> {
            val jsonObject = JSONObject(body.data)
            val sendName = jsonObject.getStringOrNull("sendName").orEmpty()
            val receiveName = jsonObject.getStringOrNull("receiveName").orEmpty()
            val giftName = jsonObject.getStringOrNull("giftName").orEmpty()
            val giftCount = jsonObject.getIntOrNull("giftCount") ?: 0
            Text(
                text = "$sendName send $receiveName ${giftName} x ${giftCount}",
                fontSize = 12.sp,
                color = Color.White,
            )
        }
    }
}