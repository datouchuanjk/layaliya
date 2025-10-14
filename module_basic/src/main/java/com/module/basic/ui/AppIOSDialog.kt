package com.module.basic.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppIOSDialog(
    isShow: Boolean,
    title: String,
    message: String,
    leftText: String = "cancel",
    rightText: String = "submit",
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
) {
    if (isShow) {
        AppDialog(
            usePlatformDefaultWidth = false,
            onDismissRequest = {},
        ) {
            Surface(
                modifier = Modifier.padding(horizontal = 15.dp),
                shape = RoundedCornerShape(14.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // 消息内容
                    Text(
                        text = message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )

                    // 分隔线
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = (16).dp),
                        color = Color(0xFFEEEEEE),
                        content = {},
                        tonalElevation = 0.dp
                    )

                    // 按钮行
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = onLeftClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = leftText,
                                color = Color(0xFF007AFF),
                                fontSize = 17.sp
                            )
                        }

                        // 按钮间的分隔线
                        Surface(
                            modifier = Modifier
                                .width(1.dp)
                                .height(48.dp),
                            color = Color(0xFFEEEEEE)
                        ) {
                            // 空内容，仅作为分隔线
                        }

                        // 确定按钮
                        TextButton(
                            onClick = {
                                onRightClick()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = rightText,
                                color = Color(0xFF007AFF),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}