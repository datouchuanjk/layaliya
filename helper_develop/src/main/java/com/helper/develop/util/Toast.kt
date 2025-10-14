package com.helper.develop.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(@StringRes res: Int, append: (() -> String)? = null) {
    val appendString = append?.invoke().orEmpty()
    Toast.makeText(this, "${resources.getString(res)}${appendString}", Toast.LENGTH_SHORT).show()
}

