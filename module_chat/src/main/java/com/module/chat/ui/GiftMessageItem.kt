package com.module.chat.ui

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import com.helper.im.data.*
import com.module.basic.sp.AppGlobal
import com.module.basic.ui.AppImage
import org.json.JSONObject

@Composable
internal fun LazyItemScope.GiftMessageItem(item: IMMessage) {
    val body = item.body as IMGiftBody
    val jsonObject = JSONObject(body.string)
    val giftId = jsonObject.getString("giftId").orEmpty()
    BasicMessageItem(item) {
        AppImage(
            model = AppGlobal.getGiftImageFileById(giftId),
        )
    }
}