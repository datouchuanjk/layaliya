package com.module.chat.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.helper.im.data.IMImageBody
import com.helper.im.data.IMMessage
import com.module.basic.ui.AppImage

@Composable
internal fun LazyItemScope.ImageMessageItem(item: IMMessage) {
    val body = item.body as IMImageBody
    BasicMessageItem(item) {
        AppImage(
            model = body.path ?: body.url,
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(body.width / body.height.toFloat())
                .clip(RoundedCornerShape(20.dp))
        )
    }
}