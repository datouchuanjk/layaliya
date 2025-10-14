package com.module.chatroom.ui.popup.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.basic.ui.AppImage
import com.module.basic.ui.AppPopup
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.paging.AppPagingBox
import com.module.basic.ui.paging.items
import com.module.basic.util.onClick
import  com.module.chatroom.R
import com.module.chatroom.api.data.response.ChatroomUserResponse
import com.module.chatroom.viewmodel.ChatRoomViewModel

@Composable
internal fun AdminListPopup(
    viewModel: ChatRoomViewModel,
    isShow: Boolean,
    onDismissRequest: () -> Unit
) {
    if (isShow) {
        LaunchedEffect(Unit) {
            viewModel.adminList.refresh()
        }
        AppPopup(
            onDismissRequest = onDismissRequest,
            popupPosition = { anchorBounds: IntRect, windowSize: IntSize, popupContentSize: IntSize ->
                IntOffset(0, anchorBounds.bottom + 5.dp.roundToPx())
            },
        ) {
            val pagingData = viewModel.adminList
            LaunchedEffect(pagingData) {
                pagingData.refresh()
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .background(color = Color.Black.copy(0.75f), shape = RoundedCornerShape(20.dp))
                    .padding(vertical = 15.dp)
            ) {
                Top()
                SpacerHeight(12.dp)
                AppPagingBox(
                    modifier = Modifier.fillMaxSize(),
                    pagingData = pagingData,
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            pagingData = pagingData,
                            key = { it.uid },
                            emptyContent = { EmptyItem() }) { item ->
                            Item(viewModel = viewModel, item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.EmptyItem() {
    Column(
        modifier = Modifier.fillParentMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppImage(
            model = R.drawable.room_ic_empty_list
        )
        Text(
            text = stringResource(R.string.room_no_record_for_the_time_being),
            fontSize = 16.sp,
            color = Color(0xff999999)
        )
    }
}

@Composable
private fun Top() {
    val list = stringArrayResource(R.array.room_admin_title)
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        repeat(5) {
            Text(
                list[it],
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth()
            )
        }
    }
}

@Composable
private fun LazyItemScope.Item(
    viewModel: ChatRoomViewModel,
    item: ChatroomUserResponse
) {
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            if (item.isMysteriousPerson ==1) stringResource(R.string.room_no_body_nickname) else item.uid.toString(),
            fontSize = 10.sp,
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth()
        )
        Text(
            text =if (item.isMysteriousPerson ==1) stringResource(R.string.room_no_body_nickname) else  item.nickname.orEmpty(),
            fontSize = 10.sp,
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth()
        )
        Text(
            item.joinTime.toString(),
            fontSize = 10.sp,
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth()
        )
        Text(
            item.joinTime.toString(),
            fontSize = 10.sp,
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth()
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .onClick {
                    viewModel.unAdmin(item.uid.toString())
                }
                .wrapContentWidth(),
            text = stringResource(R.string.room_relieve),
            fontSize = 10.sp,
            color = Color(0xff00CD26)
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(1.dp),
        color = Color.White.copy(0.1f)
    )
}