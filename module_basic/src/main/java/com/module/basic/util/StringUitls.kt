package com.module.basic.util

import androidx.core.net.toUri
import kotlin.text.toFloat

val String.aspectRatio: Float
    get() {
        toUri().run {
            val w = getQueryParameter("w")
            val h = getQueryParameter("h")
            return if (w == null || h == null) {
                1f
            } else {
                w.toFloat() / h.toFloat()
            }
        }
    }