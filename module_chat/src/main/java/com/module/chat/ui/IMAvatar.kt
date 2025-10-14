package com.module.chat.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.module.basic.ui.AppImage
import com.module.chat.R

@Composable
internal fun IMAvatar(
    model: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
    onClickable: (() -> Unit)? = null
) {
    AppImage(
        model = model ?: R.drawable.chat_ic_default_avatar,
        modifier = modifier,
        contentScale = contentScale,
        contentDescription = contentDescription,
        onClickable = onClickable
    )
}