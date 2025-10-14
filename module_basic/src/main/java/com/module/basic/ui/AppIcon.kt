package com.module.basic.ui

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.module.basic.util.onClick

@Composable
fun AppIcon(
    @DrawableRes res: Int,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    contentDescription: String? = null,
    onClickable: (() -> Unit)? = null
) {
    Icon(
        tint = tint ?: LocalContentColor.current,
        modifier = modifier.then(if (onClickable == null) Modifier else Modifier.onClick {
            onClickable.invoke()
        }),
        painter = painterResource(res),
        contentDescription = contentDescription,
    )
}
