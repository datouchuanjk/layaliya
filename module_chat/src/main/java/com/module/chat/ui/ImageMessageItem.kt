package com.module.chat.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.helper.develop.nav.LocalNavController
import com.helper.im.data.IMImageBody
import com.helper.im.data.IMMessage
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppImage

@Composable
internal fun LazyItemScope.ImageMessageItem(item: IMMessage, imagesBlock: () -> List<String>) {
    val body = item.body as IMImageBody
    val localNav = LocalNavController.current
    BasicMessageItem(item) {
        AppImage(
            model = body.path ?: body.url,
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(body.width / body.height.toFloat())
                .clip(RoundedCornerShape(20.dp))
        ) {
            val images = imagesBlock()
            localNav.navigate(
                AppRoutes.BigImage.dynamic(
                    "index" to images.indexOf(body.url),
                    "images" to images.joinToString(",")
                )
            )
        }
    }
}