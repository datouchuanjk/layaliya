package com.helper.develop.util

import android.webkit.MimeTypeMap
import androidx.core.net.toUri

val String.extension: String? get() = MimeTypeMap.getFileExtensionFromUrl(this)

val String.mimeType: String? get() = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

fun String.filterChinese(): String {
    return filter { it.isChinese }
}

fun String.filterEnglish(): String {
    return filter { it.isEnglish }
}

fun String.filterDigit(): String {
    return filter { it.isDigit() }
}

val Char.isChinese get() = this in '\u4e00'..'\u9fa5'

val Char.isEnglish get() = this in 'a'..'z' || this in 'A'..'Z'




