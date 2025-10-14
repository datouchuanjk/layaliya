package com.module.chat.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.im.data.*
import com.module.basic.sp.*
import com.module.basic.ui.*
import com.module.basic.util.todoImageUrl
import org.json.JSONObject

@Composable
internal fun LazyItemScope.GiftMessageItem(item: IMMessage) {
    val body = item.body as IMGiftBody
    val jsonObject = JSONObject(body.string)
    val sendUid = jsonObject.getString("sendUid").orEmpty()
    val sendName = jsonObject.getString("sendName").orEmpty()
    val sendAvatar = jsonObject.getString("sendAvatar").orEmpty()
    val receiveUid = jsonObject.getString("receiveUid").orEmpty()
    val receiveName = jsonObject.getString("receiveName").orEmpty()
    val receiveAvatar = jsonObject.getString("receiveAvatar").orEmpty()
    val giftName = jsonObject.getString("giftName").orEmpty()
    val giftCount = jsonObject.getInt("giftCount")
    val text = "${sendName} send ${receiveName} ${giftName}x${giftCount}"
    BasicMessageItem(item) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (item.isSelf) Color.White else Color(0xff333333),
            modifier = Modifier
                .background(
                    color = if (item.isSelf) Color(0xffFF5A96) else Color.White,
                    shape = if (item.isSelf) {
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 4.dp
                        )
                    } else {
                        RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 12.dp
                        )
                    }
                )
                .padding(vertical = 7.dp, horizontal = 12.dp)
        )
    }
}