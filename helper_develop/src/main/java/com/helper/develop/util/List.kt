package com.helper.develop.util

fun <T> List<T>.findIndex(predicate: (T) -> Boolean) = withIndex().find {
    predicate(it.value)
}?.index