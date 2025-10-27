package com.module.chat.ui

import android.util.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helper.develop.nav.*
import com.helper.develop.util.CustomActivityResultContracts
import com.helper.develop.util.copyAsFile
import com.helper.develop.util.launchImageOnly
import com.helper.im.data.IMGiftBody
import com.helper.im.data.IMImageBody
import com.helper.im.data.IMInvitationBody
import com.helper.im.data.IMTextBody
import com.helper.im.data.IMUnknownBody
import com.module.basic.constant.AppConstant.AUTHORITY
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppBottomPickVisualSelected
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.AppTitleBar
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.util.appBrushBackground
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.basic.util.onClick
import com.module.chat.R
import com.module.chat.viewmodel.ChatViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.io.File


fun NavGraphBuilder.chatScreen() = composable(
    route = AppRoutes.Chat.static,
    arguments = AppRoutes.Chat.arguments
) {
    ChatScreen()
}

@Composable
internal fun ChatScreen(viewModel: ChatViewModel = apiHandlerViewModel()) {
    val localNav = LocalNavController.current
    LaunchedEffect(Unit) {
        localNav.collectResult<String>("send_gift_result") {
            viewModel.handleGift(it)
        }
    }
    val userInfo by viewModel.userInfo.collectAsState()

    Scaffold(
        topBar = {
            AppTitleBar(text = userInfo?.name.orEmpty())
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xffF5F5F5))
                .padding(innerPadding)
        ) {
            ChatList(viewModel = viewModel)
            ChatAction(
                viewModel = viewModel,
                onGift = {
                    localNav.navigate(
                        AppRoutes.Gift.dynamic(
                            "yxIds" to listOf(viewModel.targetId).joinToString(","),
                        )
                    )
                },
            )
        }
    }
}

@Composable
private fun ColumnScope.ChatList(
    viewModel: ChatViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        var isShowScrollToBottomButton by remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.receiveMessagesFlow.collect {
                if (it) {
                    withFrameMillis {}
                    viewModel.lazyState.animateScrollToItem(0)
                } else {
                    isShowScrollToBottomButton = true
                }
            }
        }

        LaunchedEffect(viewModel.lazyState) {
            snapshotFlow { viewModel.lazyState.firstVisibleItemIndex }
                .filter { it == 0 }
                .collectLatest {
                    isShowScrollToBottomButton = false
                }
        }
        val focusManager = LocalFocusManager.current
        LazyColumn(
            state = viewModel.lazyState,
            reverseLayout = true,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        focusManager.clearFocus()
                    })
                },
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            val pagingDate = viewModel.pagingData
            itemsIndexed(
                pagingData = pagingDate, key = { it.messageId },
                emptyContent = null,
                loadErrorContent = null,
                notLoadingContent = null
            ) { _, item ->
                viewModel.sendP2PMessageReceipt(item)
                when (val body = item.body) {
                    is IMTextBody -> {
                        TextMessageItem(item)
                    }

                    is IMImageBody -> {
                        ImageMessageItem(item)
                    }

                    is IMGiftBody -> {
                        GiftMessageItem(item)
                    }

                    is IMInvitationBody -> {
                        InvitationMessageItem(item) {
                            viewModel.acceptInvite(it)
                        }
                    }

                    is IMUnknownBody -> {
                        UnknownMessageItem(item)
                    }
                }
            }
        }
        androidx.compose.animation.AnimatedVisibility(
            visible = isShowScrollToBottomButton,
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 25.dp)
        ) {
            AppImage(R.drawable.chat_ic_scroll_to_bottom) {
                scope.launch {
                    viewModel.lazyState.animateScrollToItem(0)
                    isShowScrollToBottomButton = false
                }
            }
        }
    }
}

@Composable
private fun ChatAction(
    viewModel: ChatViewModel,
    onGift: () -> Unit,
) {
    Column(
        modifier = Modifier
            .imePadding()
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(12.dp)
    ) {
        var isShowMore by remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(color = Color(0xfff5f5f5), shape = RoundedCornerShape(20.dp))
            ) {
                BasicTextField(
                    modifier = Modifier
                        .onFocusChanged {
                            if (it.isFocused) {
                                scope.launch { viewModel.lazyState.animateScrollToItem(0) }
                            }
                        }
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    value = viewModel.input,
                    onValueChange = viewModel::input,
                    decorationBox = {
                        if (viewModel.input.isEmpty()) {
                            Text(
                                stringResource(R.string.chat_send_a_message),
                                fontSize = 12.sp,
                                color = Color(0xff808080)
                            )
                        }
                        it()
                    }
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(30.dp)
                        .appBrushBackground(
                            shape = RoundedCornerShape(16.dp)
                        )
                        .onClick {
                            viewModel.sendText()
                            scope.launch {
                                withFrameMillis {}
                                viewModel.lazyState.animateScrollToItem(0)
                            }
                        }
                        .padding(horizontal = 11.dp, vertical = 3.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.chat_ic_chat_send),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                }
                SpacerWidth(12.dp)
            }
            SpacerWidth(12.dp)
            AppImage(
                if (isShowMore) R.drawable.chat_ic_action_close_menu else R.drawable.chat_ic_action_open_menu,
                modifier = Modifier.size(28.dp)
            ) {
                isShowMore = !isShowMore
            }
        }
        ChatActionMore(isShowMore, onSendImage = viewModel::sendImage, onGift = onGift)
    }
}

@Composable
private fun ChatActionMore(
    isShow: Boolean,
    onSendImage: (File) -> Unit,
    onGift: () -> Unit,
) {
    var isShowDialog by remember {
        mutableStateOf(false)
    }
    AppBottomPickVisualSelected(
        isShow = isShowDialog,
        onDismissRequest = {
            isShowDialog = false
        },
        onResult = {
            onSendImage(it[0])
        }
    )
    AnimatedVisibility(
        visible = isShow,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 33.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val list = listOf(
                stringResource(R.string.chat_photo) to R.drawable.chat_ic_action_photo,
//                stringResource(R.string.chat_take_photo) to R.drawable.chat_ic_action_take_photo,
                stringResource(R.string.chat_gift) to R.drawable.chat_ic_action_send_gift,
            )
            repeat(list.size) {
                val item = list[it]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.onClick {
                        when (it) {
                            0 -> {
                                isShowDialog = true
                            }

                            1 -> {
                                onGift()
                            }
                        }
                    }) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = Color(0xfff5f5f5),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        AppImage(model = item.second)
                    }
                    SpacerHeight(4.dp)
                    Text(text = item.first, fontSize = 14.sp, color = Color(0xff333333))
                }
            }
        }
    }
}




