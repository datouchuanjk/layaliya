package com.helper.develop.paging

sealed class LoadState(
    val endOfPaginationReached: Boolean
) {
    class NotLoading(endOfPaginationReached: Boolean) : LoadState(endOfPaginationReached) {

        override fun equals(other: Any?): Boolean {
            return other is NotLoading && endOfPaginationReached == other.endOfPaginationReached
        }
        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    data object Loading : LoadState(false)

    class Error(val error: Throwable) : LoadState(false) {

        override fun equals(other: Any?): Boolean {
            return other is Error && error == other.error
        }

        override fun hashCode(): Int {
            return error.hashCode()
        }
    }
}