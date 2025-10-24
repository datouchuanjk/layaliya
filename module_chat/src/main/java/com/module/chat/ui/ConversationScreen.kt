package com.module.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.develop.nav.LocalNavController
import com.helper.im.IMHelper
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.AppTitleBar
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.basic.util.onClick
import com.module.chat.*
import com.module.chat.viewmodel.ConversationViewModel

@Composable
fun Home2() {
    ConversationScreen()
}
@Composable
internal fun ConversationScreen(viewModel: ConversationViewModel = apiHandlerViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTitleBar(text = stringResource(R.string.chat_message), showBackIcon = false) {
//            AppImage(R.drawable.chat_ic_topbar_action_search, modifier = Modifier.size(24.dp))
//            SpacerWidth(20.dp)
//            AppImage(R.drawable.chat_ic_topbar_action_add, modifier = Modifier.size(24.dp))
        }
        val pagingData = viewModel.pagingData
        val localNavController = LocalNavController.current
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(pagingData = pagingData, key = { item ->
                item.conversationId
            }) { _, item ->
                Row(
                    modifier = Modifier
                        .animateItem()
                        .fillParentMaxWidth()
                        .onClick {
                            localNavController.navigate(
                                AppRoutes.Chat.dynamic(
                                    "conversationId" to item.conversationId,
                                )
                            )
                        }
                ) {
                    Box {
                        IMAvatar(
                            item.avatar,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.BottomEnd)
                                .size(8.dp)
                                .background(color =if(item.online)  Color(0xff30f130) else  Color(0xfff5f5f5), shape = CircleShape)
                        )
                    }
                    SpacerWidth(12.dp)
                    Column {
                        Row {
                            Column(modifier = Modifier.weight(1f)) {
                                SpacerHeight(5.dp)
                                Text(
                                    fontWeight = FontWeight.Bold,
                                    text = item.name.orEmpty(),
                                    fontSize = 14.sp,
                                    color = Color(0xff333333),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                SpacerHeight(2.dp)
                                Text(
                                    item.text.orEmpty(),
                                    fontSize = 12.sp,
                                    color = Color(0xff666666),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            SpacerWidth(12.dp)
                            Column(horizontalAlignment = Alignment.End) {
                                SpacerHeight(5.dp)
                                Text(
                                    item.time,
                                    fontSize = 12.sp,
                                    color = Color(0xff666666),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                SpacerHeight(4.dp)
                                Box(
                                    modifier = Modifier
                                        .alpha(if (item.isShowUnreadCount) 1f else 0f)
                                        .size(17.dp)
                                        .clip(CircleShape)
                                        .background(color = Color(0xffF13D30)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(item.unreadCount, fontSize = 10.sp, color = Color.White)
                                }
                            }
                        }
                            HorizontalDivider(
                                thickness = 0.1.dp,
                                color = Color(0xff333333),
                                modifier = Modifier.padding(top = 12.dp)
                            )
                    }
                }
            }
        }
    }
}