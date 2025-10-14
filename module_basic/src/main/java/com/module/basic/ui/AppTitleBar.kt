package com.module.basic.ui

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 通用头部
 */
@Composable
fun AppTitleBar(
    modifier: Modifier = Modifier,
    text: String,
    showBackIcon: Boolean = true,
    showLine: Boolean = true,
    height: Dp = 44.dp,
    textStyle: TextStyle = TextStyle(
        fontSize = 20.sp,
        color = Color(0xff333333),
        fontWeight = FontWeight.SemiBold
    ),
    backIconTint: Color? = null,
    onBack: (OnBackPressedDispatcherOwner?) -> Unit = {
        it?.onBackPressedDispatcher?.onBackPressed()
    },
    action: @Composable RowScope.() -> Unit = {},
) {
    val onBackPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(height)
                .padding(horizontal = 15.dp)
        ) {
            if (showBackIcon) {
                AppBackIcon(
                    modifier = Modifier.align(alignment = Alignment.CenterStart),
                    tint = backIconTint
                ) {
                    onBack(onBackPressedDispatcherOwner)
                }
            }
            Text(
                text = text,
                style = textStyle,
                modifier = Modifier.align(alignment = Alignment.Center)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(alignment = Alignment.CenterEnd),
                content = action
            )
        }
        if (showLine) {
            HorizontalDivider(thickness = 1.dp, color = Color(0xffE6E6E6))
        }
    }
}