package com.module.chatroom.ui.mic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.chatroom.api.data.response.ChatroomInfoResponse
import com.module.chatroom.viewmodel.ChatRoomViewModel

@Composable
internal fun Mic16(
    modifier: Modifier = Modifier,
    viewModel: ChatRoomViewModel,
    popupBox: @Composable (Boolean, ChatroomInfoResponse.MikeInfo, () -> Unit) -> Unit,
) {
    val mikeInfos = viewModel.chatroomInfoResponse?.mikeInfo
    Column(modifier = modifier) {
        Row {
         Column {
             MicItem(
                 viewModel = viewModel,
                 dp = 48.dp,
                 index = 3,
                 item = mikeInfos?.find { it?.index == 3 },
                 popupBox = popupBox
             )
             SpacerHeight(12.dp)
             MicItem(
                 viewModel = viewModel,
                 dp = 48.dp,
                 index = 5,
                 item = mikeInfos?.find { it?.index == 5 },
                 popupBox = popupBox
             )
         }
            Row(modifier = Modifier.weight(1f).wrapContentWidth(align = Alignment.CenterHorizontally).align(alignment = Alignment.Bottom).padding(bottom = 5.dp)){
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
                    index = 2,
                    item = mikeInfos?.find { it?.index == 2 },
                    popupBox = popupBox
                )
            }
            Column {
                MicItem(
                    viewModel = viewModel,
                    dp = 48.dp,
                    index = 4,
                    item = mikeInfos?.find { it?.index == 4 },
                    popupBox = popupBox
                )
                SpacerHeight(12.dp)
                MicItem(
                    viewModel = viewModel,
                    dp = 48.dp,
                    index = 6,
                    item = mikeInfos?.find { it?.index == 6 },
                    popupBox = popupBox
                )
            }
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            maxItemsInEachRow = 5,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(10) {
            val index = it + 7
            MicItem(
                viewModel = viewModel,
                dp = 48.dp,
                index = index,
                item = mikeInfos?.find { it?.index == index },
                popupBox = popupBox
            )
        }
        }
    }
}