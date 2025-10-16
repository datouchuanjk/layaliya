package com.helper.develop.paging

data class LoadResult<Key, Value>(
    val nextKey: Key?,
    val data: List<Value>?
){
    companion object{
        fun<Value> empty() = LoadResult(null,emptyList<Value>())
    }
}
