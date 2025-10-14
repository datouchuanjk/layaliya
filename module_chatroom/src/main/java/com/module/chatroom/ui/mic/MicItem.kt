package com.module.chatroom.ui.mic

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.basic.sp.AppGlobal
import com.module.basic.ui.AppBottomSelectedDialog
import com.module.basic.ui.AppFileImage
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.util.onClick
import com.module.chatroom.R
import com.module.chatroom.api.data.response.ChatroomInfoResponse
import com.module.chatroom.viewmodel.ChatRoomViewModel

@Composable
internal fun MicItem(
    viewModel: ChatRoomViewModel,
    dp: Dp,
    index: Int,
    item: ChatroomInfoResponse.MikeInfo? = null,
    popupBox: @Composable (Boolean, ChatroomInfoResponse.MikeInfo, () -> Unit) -> Unit,
) {
    val isEmpty = item == null || item.uid == 0
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(dp)
    ) {
        if (isEmpty) {
            var isShowConfirmationDialog by remember {
                mutableStateOf(false)
            }
            AppBottomSelectedDialog(
                text = stringResource(R.string.room_up_seat),
                isShow = isShowConfirmationDialog,
                onDismissRequest = {
                    isShowConfirmationDialog = false
                },
                onSubmit = {
                    viewModel.upSeat(item?.id.toString())
                }
            )
            Box {
                Image(
                    modifier = Modifier
                        .size(dp)
                        .clip(CircleShape)
                        .onClick {
                            isShowConfirmationDialog = true
                        },
                    painter = painterResource(R.drawable.room_bg_chat_empty),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
                Image(
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .size(24.dp)
                        .clip(CircleShape),
                    painter = painterResource(R.drawable.room_ic_chat_empty),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        } else {
            item ?: return
            var isShow by remember {
                mutableStateOf(false)
            }
            var isShowConfirmationDialog by remember {
                mutableStateOf(false)
            }
            AppBottomSelectedDialog(
                text = stringResource(R.string.room_down_seat),
                isShow = isShowConfirmationDialog,
                onDismissRequest = {
                    isShowConfirmationDialog = false
                },
                onSubmit = {
                    viewModel.downSeat(item.id.toString())
                }
            )
            val localDensity = LocalDensity.current
            val size = with(localDensity) {
                dp.toPx()
            }
            Box(
                modifier = Modifier
                    .size(dp)
                    .onGloballyPositioned {
                        viewModel.setMyMikeInfoOffset(item.uid, it.positionInRoot(), size)
                    }
                    .onClick {
                        if (AppGlobal.userResponse?.id == item.uid) {
                            isShowConfirmationDialog = true
                        } else {
                            isShow = true
                        }
                    }
            ) {
                if (item.emojiId.isNullOrEmpty()) {
                    AppImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        model = item.avatar,
                    )
                } else {
                    AppFileImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        model = AppGlobal.getEmojiFileById(item.emojiId),
                    )
                }

                if (item.status == 0) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(alignment = Alignment.BottomEnd)
                            .padding(bottom = 0.dp, end = 0.dp)
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(color = Color.White)
                    ) {
                        val transition = rememberInfiniteTransition()
                        val animationScale by transition.animateFloat(
                            initialValue = 0.8f,
                            targetValue = 1.2f,
                            animationSpec = InfiniteRepeatableSpec(
                                animation = tween(300),
                                repeatMode = RepeatMode.Reverse
                            )
                        )
                        val isAudio = item.uid?.toLong() in viewModel.audioStartUids
                        AppImage(
                            modifier = Modifier.scale(if (isAudio) animationScale else 1f),
                            model = R.drawable.room_ic_chat_start_audio,
                        )
                    }
                }
                popupBox(isShow, item) {
                    isShow = false
                }
            }
        }
        SpacerHeight(4.dp)
        Text(
            text = if (isEmpty) "No${index}" else item?.nickname.orEmpty(),
            fontSize = 11.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}