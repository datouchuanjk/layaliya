package com.module.chatroom.ui.mic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.basic.ui.SpacerHeight
import com.module.chatroom.api.data.response.ChatroomInfoResponse
import com.module.chatroom.viewmodel.ChatRoomViewModel

@Composable
internal fun Mic13(
    modifier: Modifier = Modifier,
    viewModel: ChatRoomViewModel,
    popupBox: @Composable (Boolean, ChatroomInfoResponse.MikeInfo, () -> Unit) -> Unit,
) {
    val mikeInfos = viewModel.chatroomInfoResponse?.mikeInfo
    Column(modifier = modifier) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 5,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            repeat(5) {
            val index = it + 2
            MicItem(
                viewModel = viewModel,
                dp = 40.dp,
                index = index,
                item = mikeInfos?.find { it?.index == index },
                popupBox = popupBox
            )
        }
        }
        SpacerHeight(12.dp)
        Row(verticalAlignment = Alignment.CenterVertically){
            MicItem(
                viewModel = viewModel,
                dp = 40.dp,
                index = 7,
                item = mikeInfos?.find { it?.index == 7 },
                popupBox = popupBox
            )
            Box(modifier= Modifier.weight(1f), contentAlignment = Alignment.Center){
                MicItem(
                    viewModel = viewModel,
                    dp = 60.dp,
                    index = 1,
                    item = mikeInfos?.find { it?.index == 1 },
                    popupBox = popupBox
                )
            }
            MicItem(
                viewModel = viewModel,
                dp = 40.dp,
                index = 8,
                item = mikeInfos?.find { it?.index == 8 },
                popupBox = popupBox
            )
        }
        SpacerHeight(12.dp)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 5,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            repeat(5) {
                val index = it + 9
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