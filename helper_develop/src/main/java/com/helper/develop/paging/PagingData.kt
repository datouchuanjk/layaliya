package com.helper.develop.paging


class PagingData<T> internal constructor(
    private val list: MutableList<T>,
    private val onRefresh: () -> Unit,
    private val onRetry: () -> Unit,
    private val onHint: (Int, Int) -> Unit,
    private val refreshStateBlock: () -> LoadState,
    private val appendStateBlock: () -> LoadState,
    private val isFirstRefreshBlock: () -> Boolean
) {
    val refreshState get() = refreshStateBlock()
    val appendState get() = appendStateBlock()
    val isFirstRefresh get() = isFirstRefreshBlock()

    fun refresh() {
        onRefresh()
    }

    fun retry() {
        onRetry()
    }

    val count get() = list.size
    val lastIndex get() = list.lastIndex

    operator fun get(index: Int): T {
        onHint(index, list.size)
        return list[index]
    }

    fun  isEmpty() = list.isEmpty()
    fun peek(index: Int): T {
        return list[index]
    }

    fun filterToPagingData(predicate: (T) -> Boolean): PagingData<T> {
        return PagingData(
            list = list.filter(predicate).toMutableList(),
            onRefresh = onRefresh,
            onRetry = onRetry,
            onHint = onHint,
            refreshStateBlock = refreshStateBlock,
            appendStateBlock = appendStateBlock,
            isFirstRefreshBlock = isFirstRefreshBlock,
        )
    }

    fun handle(block: MutableList<T>.() -> Unit) {
        block(list)
    }

    fun map(block: (T) -> T) {
        list.forEachIndexed { index, t ->
            list[index] = block(t)
        }
    }


    fun find(predicate: (T) -> Boolean): T? {
        return list.find(predicate)
    }
    fun filter(predicate: (T) -> Boolean): List<T> {
        return list.filter (predicate)
    }
}