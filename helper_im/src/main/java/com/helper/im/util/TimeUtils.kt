package com.helper.im.util

import com.helper.develop.util.hourOfDay
import com.helper.develop.util.minute
import com.helper.develop.util.second
import java.util.Calendar

internal fun Long.format() :String {
    val c = Calendar.getInstance()
    c.timeInMillis = this
    val hourOfDay = c.hourOfDay.toString().padStart(2,'0')
    val minute = c.minute.toString().padStart(2,'0')
    val second = c.second.toString().padStart(2,'0')
    return "${hourOfDay}:${minute}:${second}"
}