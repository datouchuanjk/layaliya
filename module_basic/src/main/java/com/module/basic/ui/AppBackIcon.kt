package com.module.basic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.module.basic.R

@Composable
fun AppBackIcon(
    modifier: Modifier = Modifier,
    tint: Color? = null,
    contentDescription: String? = null,
    onClickable: (() -> Unit)? = null
) {
    AppIcon(
        tint=tint,
        res = R.drawable.basic_ic_back,
        modifier = modifier.size(20.dp),
        contentDescription = contentDescription,
        onClickable = onClickable
    )
}