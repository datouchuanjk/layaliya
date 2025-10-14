package com.module.chatroom.ui.mic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.module.chatroom.api.data.response.ChatroomInfoResponse
import com.module.chatroom.ui.popup.UserInfoPopup
import com.module.chatroom.viewmodel.ChatRoomViewModel

@Composable
internal fun Mic(
    viewModel: ChatRoomViewModel,
    modifier: Modifier = Modifier,
     onShowSilencePickerDialog:()-> Unit
) {
    val count = viewModel.chatroomInfoResponse?.roomInfo?.mikeNum ?: 10
    val popupBox: @Composable (Boolean, ChatroomInfoResponse.MikeInfo, () -> Unit) -> Unit =
        @Composable { isShow, mikeInfo, onDismissRequest ->
            LaunchedEffect(viewModel) {
                viewModel.kickoutUserFlow
                    .collect {
                        //我ti了某个人  那么踢人完成之后 这个弹框肯定无了
                        onDismissRequest()
                    }
            }
            UserInfoPopup(
                uid = mikeInfo.uid.toString(),
                viewModel = viewModel,
                isShow = isShow,
                onSilence = {
                    onShowSilencePickerDialog()
                },
                onDismissRequest = onDismissRequest
            )
        }
    when (count) {
        10 -> Mic10(modifier, viewModel) { isShow, item, onDismissRequest ->
            popupBox(isShow, item, onDismissRequest)
        }
        12 -> Mic12(modifier, viewModel) { isShow, item, onDismissRequest ->
            popupBox(isShow, item,onDismissRequest)
        }

        13 -> Mic13(modifier, viewModel) { isShow, item, onDismissRequest ->
            popupBox(isShow, item,onDismissRequest)
        }
        15 -> Mic15(modifier, viewModel) { isShow, item, onDismissRequest ->
            popupBox(isShow, item,onDismissRequest)
        }
        16 -> Mic16(modifier, viewModel) { isShow, item, onDismissRequest ->
            popupBox(isShow, item,onDismissRequest)
        }
        20 -> Mic20(modifier, viewModel) { isShow, item, onDismissRequest ->
            popupBox(isShow, item,onDismissRequest)
        }
    }
}










