package com.helper.develop.util

fun <T> List<T>.findIndex(predicate: (T) -> Boolean) = withIndex().find {
    predicate(it.value)
}?.index

fun <T> List<T>.padToMultiple(multiple: Int, fillElement: T? = null): List<T?> {
    require(multiple > 0) { "倍数必须大于0" }
    val currentSize = this.size
    val remainder = currentSize % multiple
    if (remainder == 0) return this
    val paddingCount = multiple - remainder
    return this + List(paddingCount) { fillElement }
}
