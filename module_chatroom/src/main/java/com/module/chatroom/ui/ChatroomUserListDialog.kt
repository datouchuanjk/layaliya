package com.module.chatroom.ui

import android.view.Gravity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.setResult
import com.module.basic.route.AppRoutes
import com.module.basic.sp.AppGlobal
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.UpdateDialogWindow
import com.module.basic.ui.paging.AppPagingBox
import com.module.basic.ui.paging.items
import com.module.basic.util.*
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.chatroom.R
import com.module.chatroom.viewmodel.ChatroomUserListViewModel

fun NavGraphBuilder.chatroomUserListDialog() = dialog(
    route = AppRoutes.ChatroomUserList.static,
    arguments = AppRoutes.ChatroomUserList.arguments,
    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
) {
    val height = LocalWindowInfo.current.containerSize.height * 0.6f
    UpdateDialogWindow {
        it.gravity = Gravity.BOTTOM
        it.height = height.toInt()
    }
    ChatroomUserListDialog()
}

@Composable
internal fun ChatroomUserListDialog(viewModel: ChatroomUserListViewModel = apiHandlerViewModel()) {

    val localNav = LocalNavController.current
    val localBack = LocalOnBackPressedDispatcherOwner.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Black.copy(0.75f), shape = RoundedCornerShape(20.dp))
            .padding(vertical = 15.dp, horizontal = 15.dp)
    ) {
        Input(viewModel)
        SpacerHeight(24.dp)
        List(viewModel) {
            localNav.setResult(it)
            localBack?.onBackPressedDispatcher?.onBackPressed()
        }
    }
}

@Composable
private fun Input(viewModel: ChatroomUserListViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.room_uid), color = Color.White, fontSize = 14.sp)
        SpacerWidth(8.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(color = Color.White.copy(0.2f), shape = RoundedCornerShape(15.dp))
                .padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            AppImage(
                model = R.drawable.room_ic_search_uid
            )
            SpacerWidth(4.dp)
            BasicTextField(
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    fontSize = 10.sp,
                    color = Color(0xffcccccc)
                ),
                value = viewModel.searchUid,
                onValueChange = viewModel::searchUid,
                decorationBox = {
                    if (viewModel.searchUid.isEmpty()) {
                        Text(
                            text = stringResource(R.string.room_search_hint),
                            style = TextStyle(fontSize = 10.sp, color = Color.White)
                        )
                    }
                    it()
                }
            )
        }
        SpacerWidth(12.dp)
        Text(
            text = stringResource(R.string.room_search),
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier
                .appBrushBackground(
                    shape = RoundedCornerShape(15.dp)
                )
                .onClick {
                    viewModel.search()
                }
                .fillMaxHeight()
                .wrapContentSize()
                .padding(horizontal = 30.dp)
        )
    }
}

@Composable
private fun List(viewModel: ChatroomUserListViewModel, onClick: (String) -> Unit) {
    val pagingData = viewModel.userList
    AppPagingBox(
        pagingData = pagingData
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(pagingData = pagingData) {
                Row(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .onClick {
                            if (it.uid == AppGlobal.userResponse?.id) {
                                return@onClick
                            }
                            onClick(it.uid.toString())
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    if (it.isMysteriousPerson == 1) {
                        AppImage(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(color = Color(0xffD9D9D9)),
                            model = R.drawable.room_ic_no_body_avatar
                        )
                    } else {
                        val localNav = LocalNavController.current
                        AppImage(
                            model = it.avatar,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        ) {
                            localNav.navigate(
                                AppRoutes.PersonCenter.dynamic("uid" to it.id.toString())
                            )
                        }
                    }

                    SpacerWidth(12.dp)
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = if (it.isMysteriousPerson == 1) stringResource(R.string.room_no_body_nickname) else it.nickname.orEmpty(),
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        if (it.isMysteriousPerson != 1) {
                            SpacerHeight(2.dp)
                            Text(
                                text = "${stringResource(R.string.room_uid)}:${it.uid}",
                                fontSize = 14.sp,
                                color = Color(0xffe6e6e6),
                            )
                        }
                    }
                }
            }
        }
    }
}