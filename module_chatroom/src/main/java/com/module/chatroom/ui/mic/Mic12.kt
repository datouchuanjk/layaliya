package com.module.chatroom.ui.mic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.chatroom.api.data.response.ChatroomInfoResponse
import com.module.chatroom.viewmodel.ChatRoomViewModel

@Composable
internal fun Mic12(
    modifier: Modifier = Modifier,
    viewModel: ChatRoomViewModel,
    popupBox: @Composable (Boolean, ChatroomInfoResponse.MikeInfo, () -> Unit) -> Unit,
) {
    val mikeInfos = viewModel.chatroomInfoResponse?.mikeInfo
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
        ) {
            MicItem(
                viewModel = viewModel,
                dp = 80.dp,
                index = 1,
                item = mikeInfos?.find { it?.index == 1 },
                popupBox = popupBox
            )
            SpacerWidth(24.dp)
            MicItem(
                viewModel = viewModel,
                dp = 80.dp,
                index = 1,
                item = mikeInfos?.find { it?.index == 2 },
                popupBox = popupBox
            )
        }
        SpacerHeight(12.dp)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 5,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            repeat(10) {
            val index = it + 3
            MicItem(
                viewModel = viewModel,
                dp = 40.dp,
                index = index,
                item = mikeInfos?.find { it?.index == index },
                popupBox = popupBox
            )
        }
        }
    }
}