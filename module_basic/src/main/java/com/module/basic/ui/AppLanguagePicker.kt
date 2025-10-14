package com.module.basic.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.develop.wheel.Wheel
import com.module.basic.api.data.response.ConfigResponse
import com.module.basic.sp.AppGlobal
import com.module.basic.util.onClick

@Composable
fun AppLanguagePicker(
    modifier: Modifier = Modifier,
    onSelected: (ConfigResponse.Language) -> Unit
) {
    Column(modifier = modifier) {
        val data = remember {
            AppGlobal.configResponse?.language.orEmpty()
        }
        val state = rememberLazyListState()
        Icon(
            modifier = Modifier
                .align(alignment = Alignment.End)
                .onClick {
                    onSelected(data[state.firstVisibleItemIndex])
                }
                .padding(top = 15.dp)
                .padding(horizontal = 15.dp),
            imageVector = Icons.Default.Check,
            contentDescription = null
        )
        Row {
            Wheel(
                itemCount = data.count(),
                state = state,
            ) {
                Text(
                    text = data[it].name,
                    fontSize = 16.sp,
                    color = Color(0xff333333)
                )
            }

        }
    }
}