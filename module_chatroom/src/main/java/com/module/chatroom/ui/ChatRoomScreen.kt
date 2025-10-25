package com.module.chatroom.ui

import android.view.Gravity
import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.navigation.compose.*
import coil.compose.*
import com.helper.develop.nav.*
import com.helper.develop.util.toJson
import com.helper.develop.util.toast
import com.module.basic.route.*
import com.module.basic.sp.AppGlobal
import com.module.basic.ui.*
import com.module.basic.ui.picker.AppSilencePicker
import com.module.basic.util.*
import com.module.basic.viewmodel.*
import com.module.chatroom.R
import com.module.chatroom.ui.popup.*
import com.module.chatroom.ui.mic.Mic
import com.module.chatroom.ui.popup.list.AdminListPopup
import com.module.chatroom.ui.popup.list.KickoutListPopup
import com.module.chatroom.ui.popup.list.MuteListPopup
import com.module.chatroom.viewmodel.*

fun NavGraphBuilder.chatRoomScreen() =
    composable(route = AppRoutes.Chatroom.static, arguments = AppRoutes.Chatroom.arguments) {
        ChatRoomScreen()
    }

/**
 * 语聊房间
 */
@Composable
internal fun ChatRoomScreen(viewModel: ChatRoomViewModel = apiHandlerViewModel()) {

    Scaffold { innerPadding ->
        val localContext = LocalContext.current
        val localBack = LocalOnBackPressedDispatcherOwner.current
        val localNav = LocalNavController.current
        var isBack by remember {
            mutableStateOf(true)
        }
        LaunchedEffect(Unit) {
            viewModel.outFlow.collect {
                isBack = false
                localBack?.onBackPressedDispatcher?.onBackPressed()
            }
        }
        LaunchedEffect(Unit) {
            viewModel.receiveGiftFlow.collect {
                localNav.waitPopBackStack(AppRoutes.GiftPlay.static)
                localNav.navigate(
                    AppRoutes.GiftPlay.dynamic(
                        "json" to it
                    )
                )
            }
        }
        LaunchedEffect(Unit) {
            localNav.collectResult<String>("send_emoji_result") {
                viewModel.handleEmoji(it)
            }
        }

        LaunchedEffect(Unit) {
            localNav.collectResult<String>("send_gift_result") {
                viewModel.handleGift(it)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.closeCurrentUserSeatFlow
                .collect {
                    localContext.toast(R.string.room_close_successful)
                }
        }

        BackHandler(isBack) {
            isBack = false
            viewModel.exitRoom()
        }
        var isShowFailedDialog by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(viewModel) {
            viewModel.initFailedFlow.collect {
                isShowFailedDialog = true
            }
        }
        AppIOSDialog(
            isShow = isShowFailedDialog,
            onLeftClick = {
                isShowFailedDialog = false
                localBack?.onBackPressedDispatcher?.onBackPressed()
            },
            onRightClick = {
                isShowFailedDialog = false
                viewModel.initRoom()
            },
            leftText = "exit",
            rightText = "refresh",
            title = "Failed",
            message = "System is Failed please retry"
        )
        val localFocus = LocalFocusManager.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onClick {
                    localFocus.clearFocus()
                }
        ) {
            AppGifImage(
                model = R.raw.room_bg,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
            )
            viewModel.chatroomInfoResponse ?: return@Box
            val isShowKeyboard = LocalKeyboardHeight.current > 0.dp
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(bottom = LocalKeyboardHeight.current)
                    .padding(horizontal = 15.dp)
            ) {
                var isShowSilenceDialog by remember {
                    mutableStateOf(false)
                }
                if (isShowSilenceDialog) {
                    AppDialog(
                        usePlatformDefaultWidth = false,
                        layoutParamsSetting = {
                            it.dimAmount = 0.1f
                            it.gravity = Gravity.BOTTOM
                        }, onDismissRequest = { isShowSilenceDialog = false }
                    ) {
                        AppSilencePicker(
                            modifier = Modifier.background(
                                color = Color.White,
                                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                            )
                        ) {
                            isShowSilenceDialog = false
                            viewModel.silence(it.type)
                        }
                    }
                }
                SpacerHeight(13.dp)
                ChatRoomUserInfo(viewModel)
                SpacerHeight(11.dp)
                ChatRoomInfo(viewModel) {
                    isShowSilenceDialog = true
                }
                SpacerHeight(12.dp)
                AnimatedVisibility(visible = !isShowKeyboard) {
                    Mic(viewModel) {
                        isShowSilenceDialog = true
                    }
                }
                SpacerHeight(15.dp)
                var index by remember {
                    mutableIntStateOf(0)
                }
                MessageTab(index) {
                    index = it
                }
                SpacerHeight(10.dp)
                val stateHolder = rememberSaveableStateHolder()
                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.weight(1f).fillMaxWidth()) {
                    when (index) {
                        0 -> stateHolder.SaveableStateProvider("all") {
                            ChatroomTextMessage(
                                viewModel,
                                viewModel.pagingData
                            )
                        }

                        1 -> stateHolder.SaveableStateProvider("chat") {
                            ChatroomTextMessage(
                                viewModel,
                                viewModel.pagingData
                            )
                        }

                        2 -> stateHolder.SaveableStateProvider("me") {
                            ChatroomTextMessage(
                                viewModel,
                                viewModel.pagingData.filter { it.isSelf || it.receiverId == AppGlobal.userResponse?.imAccount }
                            )
                        }
                    }
                    Box {
                        AppImage(model = R.drawable.room_ic_game) {
                            localNav.navigate(
                                AppRoutes.Game.dynamic(
                                    "withChildScreen" to false,
                                    "roomId" to viewModel.roomId
                                )
                            )
                        }
                        Text("Game Bar", fontSize = 10.sp, color = Color.White, modifier = Modifier.align(alignment = Alignment.BottomCenter).padding(bottom = 6.dp))
                    }
                }
                ChatAction(viewModel = viewModel)
            }
        }
    }
}


@Composable
private fun ChatRoomUserInfo(viewModel: ChatRoomViewModel) {
    val roomInfo = viewModel.chatroomInfoResponse?.roomInfo
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        var isShowManage by remember {
            mutableStateOf(false)
        }
        var isShowAdminList by remember {
            mutableStateOf(false)
        }
        var isShowSilenceList by remember {
            mutableStateOf(false)
        }
        var isShowKickoutList by remember {
            mutableStateOf(false)
        }
        var isShowMore by remember {
            mutableStateOf(false)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(
                color = Color.White.copy(0.2f),
                shape = RoundedCornerShape(20.dp)
            )
        ) {
            AppImage(
                model = roomInfo?.cover,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
            )
            SpacerWidth(6.dp)
            Column {
                Text(roomInfo?.name.orEmpty(), fontSize = 14.sp, color = Color.White)
                Text("UID:${viewModel.roomId}", fontSize = 8.sp, color = Color(0xffcccccc))
            }
            SpacerWidth(4.dp)
            if(roomInfo?.isFollow !=1){
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(24.dp)
                        .appBrushBackground(
                            shape = RoundedCornerShape(12.dp)
                        )
                        .onClick {
                            if (roomInfo?.isFollow == 1) {
                                viewModel.unFollowRoom()
                            } else {
                                viewModel.followRoom()
                            }
                        }
                        .wrapContentSize()
                ) {
                    AppImage(
                        if (roomInfo?.isFollow == 1) R.drawable.room_ic_unfollow_room else R.drawable.room_ic_follow_room
                    )
                }
            }
            SpacerWidth(6.dp)
        }
        SpacerWeight(1f)
        if (viewModel.chatroomInfoResponse?.userInfo?.isMasterOrAdmin == true) {
            Image(
                painter = painterResource(R.drawable.room_ic_chat_manage),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.onClick {
                    isShowManage = true
                }
            )
        }
        SpacerWidth(24.dp)
        val nav = LocalNavController.current
        Image(
            painter = painterResource(R.drawable.room_ic_chat_report),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.onClick {
                nav.navigate(
                    AppRoutes.ChatroomReport.dynamic(
                        "type" to 1,
                        "objId" to viewModel.roomId
                    )
                )
            }
        )
        SpacerWidth(24.dp)
        Image(
            painter = painterResource(R.drawable.room_ic_chat_more),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.onClick {
                isShowMore = true
            }
        )
        val localNav = LocalNavController.current
        ManagePopup(
            isMaster = viewModel.chatroomInfoResponse?.userInfo?.isMaster == true,
            isShow = isShowManage,
            onSilenceList = { isShowSilenceList = true },
            onAdminList = { isShowAdminList = true },
            onModification = {
                val roomInfo = viewModel.chatroomInfoResponse?.roomInfo
                localNav.navigate(
                    AppRoutes.CreateOrEditRoom.dynamic(
                        "roomInfo" to roomInfo?.toJson(),
                        "isEdit" to true
                    )
                )
            },
            onKickOutList = { isShowKickoutList = true },
            onDismissRequest = { isShowManage = false }
        )
        MorePopup(
            viewModel = viewModel,
            isShow = isShowMore,
            onDismissRequest = { isShowMore = false }
        )

        AdminListPopup(
            viewModel = viewModel,
            isShow = isShowAdminList,
            onDismissRequest = { isShowAdminList = false }
        )

        MuteListPopup(
            viewModel = viewModel,
            isShow = isShowSilenceList,
            onDismissRequest = { isShowSilenceList = false }
        )

        KickoutListPopup(
            viewModel = viewModel,
            isShow = isShowKickoutList,
            onDismissRequest = { isShowKickoutList = false }
        )
    }
}

@Composable
private fun ChatRoomInfo(viewModel: ChatRoomViewModel, onShowSilencePickerDialog: () -> Unit) {
    var selectedUid by remember {
        mutableStateOf<String?>(null)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = Color.White.copy(0.2f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(vertical = 4.dp, horizontal = 6.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.room_ic_chat_like),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            SpacerWidth(4.dp)
            Text("1.1k", fontSize = 14.sp, color = Color.White)
        }
        SpacerWeight(1f)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            viewModel.chatroomUserInfos.forEach {
                Image(
                    painter = rememberAsyncImagePainter(it.avatar),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
            }
        }
        SpacerWidth(6.dp)
        val localNav = LocalNavController.current
        Text(
            viewModel.chatroomInfoResponse?.roomInfo?.displayUserNum.orEmpty(),
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .size(35.dp)
                .clip(CircleShape)
                .background(
                    color = Color.White.copy(0.2f),
                    shape = RoundedCornerShape(16.dp)
                )
                .wrapContentSize()
                .onClick {
                    localNav.navigateForResult<String>(
                        AppRoutes.ChatroomUserList.dynamic(
                            "roomId" to viewModel.roomId
                        )
                    ) {
                        selectedUid = it
                    }
                }
        )
        if (!selectedUid.isNullOrEmpty()) {
            LaunchedEffect(viewModel) {
                viewModel.kickoutUserFlow
                    .collect {
                        //我ti了某个人  那么踢人完成之后 这个弹框肯定无了
                        selectedUid = null
                    }
            }
            UserInfoPopup(
                uid = selectedUid.toString(),
                viewModel = viewModel,
                isShow = true,
                onSilence = {
                    onShowSilencePickerDialog()
                },
                onDismissRequest = {
                    selectedUid = null
                }
            )
        }
    }
}

@Composable
private fun MessageTab(selectedIndex: Int, onTabChanged: (Int) -> Unit) {
    val tabs = stringArrayResource(R.array.room_chat_message_tab)
    val selectedModifier = Modifier
        .appBrushBackground(
            shape = RoundedCornerShape(20.dp)
        )
        .padding(horizontal = 12.dp, vertical = 2.dp)
    val unselectedModifier = Modifier
        .background(
            color = Color.White.copy(0.2f), shape = RoundedCornerShape(20.dp)
        )
        .padding(horizontal = 12.dp, vertical = 2.dp)
    Row(verticalAlignment = Alignment.CenterVertically) {
        tabs.forEachIndexed { index, it ->
            Text(
                text = it,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(start = if (index == 0) 0.dp else 12.dp)
                    .then(
                        if (index == selectedIndex) {
                            selectedModifier
                        } else {
                            unselectedModifier
                        }
                    )
                    .onClick {
                        onTabChanged(index)
                    }
            )
        }
    }
}


@Composable
private fun ChatAction(
    viewModel: ChatRoomViewModel,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    var isShowInput by remember {
        mutableStateOf(true)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 10.dp)
    ) {
        if (isShowInput) {
            Input(viewModel, onHiddenInput = {
                isShowInput = false
                isFocused = false
            }, onFocusChanged = {
                isFocused = it
            })
        } else {
            Box(
                modifier = Modifier
                    .background(color = Color.White.copy(0.2f), shape = CircleShape)
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                AppImage(
                    model = R.drawable.room_ic_chat_message,
                    modifier = Modifier
                        .size(30.dp)
                ) {
                    isShowInput = true
                }
            }
            SpacerWeight(1f)
        }

        AnimatedVisibility(!isFocused) {
            val localNav = LocalNavController.current
            Row {
                SpacerWidth(10.dp)
                if (viewModel.myMikeInfo != null && viewModel.myMikeInfoOffset != null) {
                    Box(
                        modifier = Modifier
                            .background(color = Color.White.copy(0.2f), shape = CircleShape)
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AppImage(
                            model = R.drawable.room_ic_chat_ej,
                            modifier = Modifier
                                .size(30.dp)
                        ) {
                            val rect = viewModel.myMikeInfoOffset!!
                            localNav.navigate(
                                AppRoutes.Emoji.dynamic(
                                    "x" to rect.left,
                                    "y" to rect.top,
                                    "w" to rect.width,
                                    "h" to rect.height,
                                )
                            )
                        }
                    }
                }
                SpacerWidth(10.dp)
                Box(
                    modifier = Modifier
                        .background(color = Color.White.copy(0.2f), shape = CircleShape)
                        .size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AppImage(
                        model = if (viewModel.myMikeInfo != null)
                            R.drawable.room_ic_action_down_seat
                        else
                            R.drawable.room_ic_action_up_seat,
                        modifier = Modifier.size(25.dp)
                    ) {
                        viewModel.quickUpOrDownSeat()
                    }
                }
                if (viewModel.myMikeInfo != null) {
                    SpacerWidth(10.dp)
                    Box(
                        modifier = Modifier
                            .background(color = Color.White.copy(0.2f), shape = CircleShape)
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AppImage(
                            model = if (viewModel.myIsOpenMike) R.drawable.room_ic_chat_close_seat else R.drawable.room_ic_chat_open_seat,
                            modifier = Modifier
                                .size(30.dp)
                        ) {
                            if (viewModel.myIsOpenMike) {
                                viewModel.closeSeat()
                            } else {
                                viewModel.openSeat()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.Input(
    viewModel: ChatRoomViewModel,
    onHiddenInput: () -> Unit,
    onFocusChanged: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(40.dp)
            .weight(1f)
            .background(
                color = Color.White.copy(0.2f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(5.dp)
    ) {
        AppImage(
            model = R.drawable.room_ic_chat_message,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        ) {
            onHiddenInput()
        }
        SpacerWidth(4.dp)
        if (viewModel.toNickname.isNotEmpty()) {
            Text(
                modifier = Modifier,
                text = "@${viewModel.toNickname.trim()}",
                style = TextStyle(fontSize = 12.sp, color = Color.White)
            )
        }
        BasicTextField(
            value = viewModel.input,
            onValueChange = {
                viewModel.input(it)
            },
            decorationBox = {
                if (viewModel.input.isEmpty()&&viewModel.toNickname.trim().isEmpty()) {
                    Text(
                        stringResource(R.string.room_send_a_message),
                        style = TextStyle(fontSize = 12.sp, color = Color.White)
                    )
                }
                it()
            },
            textStyle = TextStyle(fontSize = 12.sp, color = Color.White),
            modifier = Modifier
                .focusRequester(viewModel.focusRequester)
                .onFocusChanged {
                    onFocusChanged(it.isFocused)
                }
                .then(if (viewModel.input.isEmpty()) Modifier.onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                        viewModel.clearToNickname()
                        true
                    } else
                        false
                } else Modifier)
                .weight(1f)
                .padding(horizontal = 5.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .appBrushBackground(
                    shape = RoundedCornerShape(16.dp)
                )
                .onClick {
                    viewModel.sendTextMessage()
                }
                .padding(horizontal = 11.dp, vertical = 3.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.room_ic_chat_send),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }
    }
}



