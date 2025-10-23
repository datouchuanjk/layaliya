package com.module.chatroom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.develop.paging.PagingData
import com.helper.develop.util.fromJson
import com.helper.im.data.IMChatroomMessage
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.paging.items
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
        AppImage(
            model = item.senderAvatar,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
        SpacerWidth(11.dp)
        Column {
            Text(item.senderName.orEmpty(), fontSize = 14.sp, color = Color.White)
            SpacerHeight(4.dp)
            Text(
                item.text.orEmpty(),
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
            val sendUid = jsonObject.getString("sendUid").orEmpty()
            val sendName = jsonObject.getString("sendName").orEmpty()
            val sendAvatar = jsonObject.getString("sendAvatar").orEmpty()
            val receiveUid = jsonObject.getString("receiveUid").orEmpty()
            val receiveName = jsonObject.getString("receiveName").orEmpty()
            val receiveAvatar = jsonObject.getString("receiveAvatar").orEmpty()
            val giftName = jsonObject.getString("giftName").orEmpty()
            val giftCount = jsonObject.getInt("giftCount")
            Text(
                text = "${sendName} send ${receiveName} ${giftName}x${giftCount}",
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}