package com.helper.develop.paging

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class PagingImpl<T, V>(
    private val scope: CoroutineScope,
    private val context: CoroutineContext,
    pagingStart: PagingStart = PagingStart.DEFAULT,
    private val initialKey: T? = null,
    private val config: PagingConfig = PagingConfig(),
    private val load: suspend (LoadParams<T>) -> LoadResult<T, V>,
) : Paging<V>, CoroutineScope by scope {

    private var _nextKey: T? = initialKey
    private val _refreshMutex = Mutex()
    private val _appendMutex = Mutex()
    private var _refreshJob: Job? = null
    private var _appendJob: Job? = null

    private val _isRefreshRunning get() = _refreshState is LoadState.Loading || _refreshJob?.isActive == true || _refreshMutex.isLocked
    private val _isAppendRunning get() = _loadMoreState is LoadState.Loading || _appendJob?.isActive == true || _appendMutex.isLocked

    private val _list = mutableStateListOf<V>()
    private var _refreshState: LoadState by mutableStateOf(LoadState.NotLoading(false))
    private var _loadMoreState: LoadState by mutableStateOf(LoadState.NotLoading(false))
    private var _isFirstRefresh: Boolean by mutableStateOf(true)

    init {
        if (pagingStart == PagingStart.DEFAULT) {
            refresh()
        }
    }

    private fun refresh() {
        if (_isRefreshRunning) return
        _appendJob?.cancel()
        _refreshJob = launch {
            _refreshMutex.withLock {
                try {
                    _refreshState = LoadState.Loading
                    _loadMoreState = LoadState.NotLoading(false)
                    val result = withContext(context) {
                        load(
                            LoadParams(
                                key = initialKey,
                                pageSize = config.pageSize,
                            )
                        )
                    }
                    _isFirstRefresh = false
                    _nextKey = result.nextKey
                    _refreshState = LoadState.NotLoading(_nextKey == null)
                    if (_nextKey == null) {
                        _loadMoreState = LoadState.NotLoading(_nextKey == null)
                    }
                    _list.clear()
                    _list.addAll(result.data.orEmpty())
                } catch (e: Exception) {
                    e.printStackTrace()
                    _refreshState = LoadState.Error(e)
                }
            }
        }
    }

    private fun load() {
        if (_isRefreshRunning) return
        if (_isAppendRunning) return
        if (_loadMoreState == LoadState.NotLoading(true)) return
        if (_loadMoreState is LoadState.Error) return
        _appendJob = launch {
            _appendMutex.withLock {
                try {
                    _loadMoreState = LoadState.Loading
                    val result = withContext(Dispatchers.IO) {
                        load(
                            LoadParams(
                                key = _nextKey,
                                pageSize = config.pageSize,
                            )
                        )
                    }
                    _nextKey = result.nextKey
                    _loadMoreState = LoadState.NotLoading(_nextKey == null)
                    _list.addAll(result.data.orEmpty())
                } catch (e: Exception) {
                    e.printStackTrace()
                    _loadMoreState = LoadState.Error(e)
                }
            }
        }
    }

    private fun retry() {
        if (_isRefreshRunning) return
        if (_loadMoreState is LoadState.Error) {
            _loadMoreState = LoadState.NotLoading(false)
            load()
        }
    }

    override val pagingData: PagingData<V>
        get() = PagingData(
            list = _list,
            onRefresh = { refresh() },
            onRetry = { retry() },
            onHint = { index, count ->
                if (index + config.prefetchDistance >= count) {
                    load()
                }
            },
            refreshStateBlock = { _refreshState },
            appendStateBlock = { _loadMoreState },
            isFirstRefreshBlock = { _isFirstRefresh }
        )
}