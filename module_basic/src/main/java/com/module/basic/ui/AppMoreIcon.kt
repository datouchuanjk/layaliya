package com.module.basic.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.module.basic.R

@Composable
fun AppMoreIcon(
    modifier: Modifier = Modifier,
    tint: Color? = null,
    contentDescription: String? = null,
    onClickable: (() -> Unit)? = null
) {
    AppIcon(
        tint=tint,
         res = R.drawable.basic_ic_more,
        modifier = modifier,
        contentDescription = contentDescription,
        onClickable = onClickable
    )
}