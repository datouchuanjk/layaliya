package com.module.chat.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.im.data.*
import com.module.basic.sp.AppGlobal
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerWidth
import org.json.JSONObject

@Composable
internal fun LazyItemScope.GiftMessageItem(item: IMMessage) {
    val body = item.body as IMGiftBody
    val jsonObject = JSONObject(body.string)
    val giftId = jsonObject.getString("giftId").orEmpty()
    val giftCount = jsonObject.getString("giftCount").orEmpty()
    BasicMessageItem(item) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (item.isSelf) {
                Text(text = "x $giftCount", fontSize = 14.sp, color = Color.Black)
                SpacerWidth(5.dp)
            }
            AppImage(
                model = AppGlobal.getGiftImageFileById(giftId),
            )
            if (!item.isSelf) {
                Text(text = "x $giftCount", fontSize = 14.sp, color = Color.Black)
                SpacerWidth(5.dp)
            }
        }
    }
}