package com.module.basic.ui.picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.develop.util.dayOfMonth
import com.helper.develop.util.dayRangeInMonth
import com.helper.develop.util.month
import com.helper.develop.util.year
import com.helper.develop.wheel.Wheel
import com.module.basic.util.onClick
import java.util.Calendar

@Composable
fun AppDatePicker(
    modifier: Modifier = Modifier,
    onSelected: (Calendar) -> Unit
) {
    Column(modifier = modifier) {
        val yearRange = remember {
            (1970..Calendar.getInstance().year).toList()
        }
        val yearState = rememberLazyListState()
        val yearIndex by remember(yearState) {
            derivedStateOf {
                yearState.firstVisibleItemIndex
            }
        }

        val monthRange = remember {
            (0..11).toList()
        }
        val monthState = rememberLazyListState()
        val monthIndex by remember(monthState) {
            derivedStateOf {
                monthState.firstVisibleItemIndex
            }
        }

        val c = remember {
            Calendar.getInstance()
        }
        val dayRange by remember(yearIndex, monthIndex) {
            c.year = yearRange[yearIndex]
            c.month = monthRange[monthIndex]
            mutableStateOf(c.dayRangeInMonth.toList())
        }
        val dayState = rememberLazyListState()
        val dayIndex by remember(dayState) {
            derivedStateOf {
                dayState.firstVisibleItemIndex
            }
        }
        Icon(
            modifier = Modifier
                .align(alignment = Alignment.End)
                .onClick {
                    val calendar = Calendar.getInstance()
                    calendar.year = yearRange[yearIndex]
                    calendar.month = monthRange[monthIndex]
                    calendar.dayOfMonth = dayRange[dayIndex]
                    onSelected(calendar)
                }
                .padding(top = 15.dp)
                .padding(horizontal = 15.dp),
            imageVector = Icons.Default.Check,
            contentDescription = null
        )
        Row {
            Wheel(
                itemCount = yearRange.count(),
                state = yearState
            ) {
                Text(
                    text = yearRange[it].toString().padStart(4, '0'),
                    fontSize = 16.sp,
                    color = Color(0xff333333)
                )
            }
            Wheel(
                itemCount = monthRange.count(),
                state = monthState
            ) {
                Text(
                    text = (monthRange[it] + 1).toString().padStart(2, '0'),
                    fontSize = 16.sp,
                    color = Color(0xff333333)
                )
            }
            Wheel(
                itemCount = dayRange.count(),
                state = dayState
            ) {
                Text(
                    text = dayRange[it].toString().padStart(2, '0'),
                    fontSize = 16.sp,
                    color = Color(0xff333333)
                )
            }
        }
    }
}