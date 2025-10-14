package com.module.chatroom.ui.popup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.develop.Background
import com.module.basic.ui.AppPopup
import com.module.basic.ui.SpacerHeight
import com.module.basic.util.onClick
import com.module.chatroom.R
import com.module.chatroom.viewmodel.ChatRoomViewModel


@Composable
internal fun MorePopup(
    viewModel: ChatRoomViewModel,
    isShow: Boolean,
    onDismissRequest: () -> Unit
) {
    if (isShow) {
        AppPopup(
            onDismissRequest = onDismissRequest,
            popupPosition = { anchorBounds: IntRect, windowSize: IntSize, popupContentSize: IntSize ->
                IntOffset(0, anchorBounds.bottom + 5.dp.roundToPx())
            },
        ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .background(color = Color.Black.copy(0.75f), shape = RoundedCornerShape(20.dp))
                        .padding(vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    class Item(
                        val text: String,
                        val image: Painter,
                        val background: Color,
                        val onClick: () -> Unit
                    )
                    stringArrayResource(R.array.room_warning_action)
                        .mapIndexed { index: Int, s: String ->
                            when (index) {
                                0 -> Item(
                                    text = s,
                                    image = painterResource(R.drawable.room_ic_chat_action_min),
                                    background = Color.White,
                                    onClick = {
                                        //最小化
                                        onDismissRequest()
                                    }
                                )

                                1 -> Item(
                                    text = s,
                                    image = painterResource(R.drawable.room_ic_chat_action_exit),
                                    background = Color(0xffFF4070),
                                    onClick = {
                                        onDismissRequest()
                                        viewModel.exitRoom()
                                    }
                                )

                                else -> throw NullPointerException()
                            }
                        }.forEach { item ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .onClick {
                                        item.onClick()
                                    }) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = item.background.copy(0.1f),
                                            shape = CircleShape
                                        )
                                        .padding(12.dp)
                                ) {
                                    Image(
                                        painter = item.image,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                    )
                                }
                                SpacerHeight(6.dp)
                                Text(
                                    item.text,
                                    fontSize = 12.sp,
                                    color = item.background
                                )
                            }
                        }
            }
        }
    }
}