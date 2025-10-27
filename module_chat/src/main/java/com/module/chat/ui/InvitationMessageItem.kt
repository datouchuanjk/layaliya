package com.module.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.im.data.IMInvitationBody
import com.helper.im.data.IMMessage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.util.*
import com.module.chat.R

@Composable
internal fun LazyItemScope.InvitationMessageItem(item: IMMessage, block: (String) -> Unit) {
    val body = item.body as IMInvitationBody
        BasicMessageItem(item) {
            if (item.isSelf) {
                Text(
                    text = "Invite ${item.receiverName} to be my anchor",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            color = Color(0xffFF5A96),
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp,
                                bottomEnd = 12.dp,
                                bottomStart = 4.dp
                            )
                        )
                        .padding(vertical = 7.dp, horizontal = 12.dp)
                )
            } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(body.content, fontSize = 16.sp, color = Color.Black)
                SpacerHeight(15.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                ) {
                    Text(
                        text = stringResource(R.string.chat_no),
                        fontSize = 14.sp,
                        color = Color(0xffcccccc),
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .border(
                                1.dp,
                                color = Color(0xffe6e6e6),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .onClick {

                            }
                            .wrapContentSize()
                    )
                    SpacerWidth(7.dp)
                    Text(
                        text = stringResource(R.string.chat_confirm),
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .appBrushBackground(
                                shape = RoundedCornerShape(12.dp)
                            )
                            .onClick {
                                block(body.recordId)
                            }
                            .wrapContentSize()
                    )
                }
            }
        }
    }
}