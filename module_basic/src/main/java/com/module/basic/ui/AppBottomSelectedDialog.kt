package com.module.basic.ui

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.basic.util.onClick

@Composable
fun AppBottomSelectedDialog(
    isShow: Boolean,
    text: String,
    onSubmit: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if(isShow){
    AppDialog(
        layoutParamsSetting = {
            it.gravity = Gravity.BOTTOM
        },
        usePlatformDefaultWidth = false,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                )
                .padding(bottom = 15.dp)
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .onClick {
                        onSubmit()
                        onDismissRequest()
                    }
                    .wrapContentWidth()
                    .padding(vertical = 15.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(color = Color(0xfff5f5f5))
            )
            Text(
                text = "Cancel",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .onClick {
                        onDismissRequest()
                    }
                    .wrapContentWidth()
                    .padding(vertical = 15.dp)
            )
        }
        }
    }
}