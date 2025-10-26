package com.module.chatroom.ui.popup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.helper.develop.Background
import com.helper.develop.nav.LocalNavController
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppImage
import com.module.basic.ui.AppPopup
import com.module.basic.ui.SpacerHeight
import com.module.basic.util.onClick
import com.module.chatroom.R
import com.module.chatroom.viewmodel.ChatRoomViewModel
import kotlin.math.min

@Composable
private fun getItems(viewModel: ChatRoomViewModel, onSilence: () -> Unit): List<PopupAction?> {
    val self = viewModel.chatroomInfoResponse?.userInfo ?: return emptyList()
    val other = viewModel.currentUserDetail ?: return emptyList()
    val otherIsFollow = other.isFollow == "1"
    val otherIsAdmin = other.isAdmin
    val otherIsSilence = other.isMuted == "1"
    val otherIsInSeat = viewModel.currentUserMikeInfo != null
    val localNav = LocalNavController.current
    val isMysteriousPerson = other.isMysteriousPerson == 1
    val myIsHeight = self.isMaster || (self.isAdmin && !other.isMasterOrAdmin)
    val followAction = PopupAction(
        image = if (isMysteriousPerson) painterResource(R.drawable.room_ic_action_gray_follow) else if (otherIsFollow)
            painterResource(R.drawable.room_ic_action_unfollow)
        else
            painterResource(R.drawable.room_ic_action_follow),
        text = if (isMysteriousPerson) stringResource(R.string.room_follow) else if (otherIsFollow) stringResource(
            R.string.room_unfollow
        ) else stringResource(R.string.room_follow),
        enable = !isMysteriousPerson,
        onClick = {
            if (otherIsFollow) {
                viewModel.unFollow()
            } else {
                viewModel.follow()
            }
        }
    )
    val mentionAction = PopupAction(
        image = painterResource(R.drawable.room_ic_action_mention),
        text = stringResource(R.string.room_mention),
        onClick = {
            viewModel.toNickname()
        }
    )
    val giftAction = PopupAction(
        image = if (isMysteriousPerson) painterResource(R.drawable.room_ic_action_gray_send_gift) else painterResource(
            R.drawable.room_ic_action_send_gift
        ),
        text = stringResource(R.string.room_send_gifts),
        enable = !isMysteriousPerson,
        onClick = {
            localNav.navigate(
                AppRoutes.Gift.dynamic(
                    "receiveUid" to other.id.toString(),
                    "receiveName" to other.nickname.toString(),
                    "receiveAvatar" to other.avatar.toString(),
                    "roomId" to viewModel.roomId
                )
            )
        }
    )
    val muteAction = PopupAction(
        image = if (!myIsHeight) painterResource(R.drawable.room_ic_action_gray_close_mute) else painterResource(
            R.drawable.room_ic_action_close_mute
        ),
        text = stringResource(R.string.room_mute),
        enable = myIsHeight,
        onClick = viewModel::closeCurrentUserSeat
    )
    val seatAction = PopupAction(
        image = if (!myIsHeight)
            painterResource(R.drawable.room_ic_action_gray_seat) else if (otherIsInSeat)
            painterResource(R.drawable.room_ic_action_down_seat)
        else
            painterResource(R.drawable.room_ic_action_up_seat),
        text = if (isMysteriousPerson) stringResource(R.string.room_up_seat) else if (otherIsInSeat) stringResource(
            R.string.room_down_seat
        ) else stringResource(R.string.room_up_seat),
        enable = myIsHeight,
        onClick = {
            if (otherIsInSeat) {
                viewModel.downUserSeat()
            } else {
                viewModel.upUserSeat()
            }
        }
    )
    val silenceAction = PopupAction(
        image = if (!myIsHeight)
            painterResource(R.drawable.room_ic_action_gray_silence) else if (otherIsSilence) painterResource(
            R.drawable.room_ic_action_unsilence
        ) else painterResource(
            R.drawable.room_ic_action_silence
        ),
        text = if (isMysteriousPerson) stringResource(R.string.room_silence) else if (otherIsSilence) stringResource(
            R.string.room_unsilence
        ) else stringResource(R.string.room_silence),
        enable = myIsHeight,
        onClick = {
            if (otherIsSilence) {
                viewModel.unsilence()
            } else {
                onSilence()
            }
        }
    )
    val adminAction = PopupAction(
        image = if (isMysteriousPerson) painterResource(R.drawable.room_ic_action_gray_admin) else if (otherIsAdmin) painterResource(
            R.drawable.room_ic_action_unadmin
        ) else painterResource(
            R.drawable.room_ic_action_add_admin
        ),
        text = if (isMysteriousPerson) stringResource(R.string.room_add_admin) else if (otherIsAdmin) stringResource(
            R.string.room_un_admin
        ) else stringResource(R.string.room_add_admin),
        enable = !isMysteriousPerson,
        onClick = {
            if (otherIsAdmin) {
                viewModel.unAdmin()
            } else {
                viewModel.addAdmin()
            }
        }
    )
    val outAction = PopupAction(
        image = painterResource(R.drawable.room_ic_action_kick_out),
        text = stringResource(R.string.room_kick_out),
        onClick = {
            viewModel.kickout()
        }
    )
    return if (self.isMaster) {
        listOf(
            followAction,
            mentionAction,
            giftAction,
            muteAction,
            seatAction,
            silenceAction,
            adminAction,
            outAction
        )
    } else if (self.isAdmin && !other.isMaster) {
        listOf(
            followAction,
            mentionAction,
            giftAction,
            muteAction,
            seatAction,
            silenceAction,
            outAction,
            null
        )
    } else {
        listOf(followAction, mentionAction, giftAction)
    }
}

@Composable
internal fun UserInfoPopup(
    uid: String,
    viewModel: ChatRoomViewModel,
    isShow: Boolean,
    onSilence: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (isShow) {
        DisposableEffect(Unit) {
            viewModel.getCurrentUserDetail(uid)
            onDispose {
                viewModel.clearCurrentUserDetail()
            }
        }
        viewModel.currentUserDetail ?: return
        AppPopup(
            onDismissRequest = onDismissRequest,
            popupPosition = { anchorBounds: IntRect, windowSize: IntSize, popupContentSize: IntSize ->
                IntOffset(anchorBounds.left, anchorBounds.bottom + 5.dp.roundToPx())
            },
        ) {

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .background(color = Color.Black.copy(0.75f), shape = RoundedCornerShape(20.dp))
                ) {
                    val isMysteriousPerson = viewModel.currentUserDetail?.isMysteriousPerson == 1
                    val (icon, name, info, warning, row) = createRefs()
                    Box(
                        modifier = Modifier
                            .constrainAs(icon) {
                                top.linkTo(parent.top, 12.dp)
                                start.linkTo(parent.start, 12.dp)
                            }
                            .size(48.dp)
                    ) {
                        if (isMysteriousPerson) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(color = Color(0xffD9D9D9)),
                                contentAlignment = Alignment.Center
                            ) {
                                AppImage(
                                    model = R.drawable.room_ic_no_body_avatar
                                )
                            }
                        } else {
                            val localNav = LocalNavController.current
                            AppImage(
                                model = viewModel.currentUserDetail?.avatar,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            ){
                                localNav.navigate(
                                    AppRoutes.PersonCenter.dynamic("uid" to viewModel.currentUserDetail?.id.toString())
                                )
                            }
                        }
                    }

                    Text(
                        if (isMysteriousPerson) stringResource(R.string.room_no_body_nickname) else viewModel.currentUserDetail?.nickname.orEmpty().trim(),
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.constrainAs(name) {
                            top.linkTo(icon.top)
                            start.linkTo(icon.end, 12.dp)
                            if(isMysteriousPerson){
                                bottom.linkTo(icon.bottom)
                            }
                        })
                  if(!isMysteriousPerson){
                      Row(
                          verticalAlignment = Alignment.CenterVertically,
                          modifier = Modifier.constrainAs(info) {
                              top.linkTo(name.bottom)
                              start.linkTo(name.start)
                              end.linkTo(parent.end, 12.dp)
                              width = Dimension.fillToConstraints
                          }
                      ) {
                          Text(
                              " UID:${viewModel.currentUserDetail?.uuid}",
                              fontSize = 14.sp,
                              color = Color(0xffe6e6e6)
                          )
                      }
                  }
                    val nav = LocalNavController.current
                    AppImage(
                        modifier = Modifier.constrainAs(warning) {
                            top.linkTo(icon.top)
                            end.linkTo(parent.end, 12.dp)
                        },
                        model = R.drawable.room_ic_chat_report
                    ) {
                        nav.navigate(
                            AppRoutes.ChatroomReport.dynamic(
                                "type" to 0,
                                "objId" to viewModel.currentUserDetail?.uuid.toString()
                            )
                        )
                    }
                  val list =   getItems(viewModel, onSilence)
                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        maxItemsInEachRow = min(list.size,4),
                        modifier = Modifier.constrainAs(row) {
                            top.linkTo(icon.bottom, 24.dp)
                            start.linkTo(parent.start, 0.dp)
                            end.linkTo(parent.end, 0.dp)
                            bottom.linkTo(parent.bottom, 12.dp)
                            width = Dimension.fillToConstraints
                        }
                    ) {
                        list.forEach {
                                if(it==null){
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .weight(1f)
                                           ) {
                                        Box(
                                            modifier = Modifier.size(32.dp),
                                        )
                                        SpacerHeight(4.dp)
                                        Text(text ="", fontSize = 12.sp, color = Color.White)
                                    }
                                }else{
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .weight(1f)
                                            .onClick(enabled = it.enable) {
                                                onDismissRequest()
                                                it.onClick()
                                            }) {
                                        Image(
                                            modifier = Modifier.size(32.dp),
                                            painter = it.image,
                                            contentScale = ContentScale.Crop,
                                            contentDescription = null,
                                        )
                                        SpacerHeight(4.dp)
                                        Text(text = it.text, fontSize = 12.sp, color = Color.White)
                                    }
                                }
                    }
                }
            }
        }
    }
}
