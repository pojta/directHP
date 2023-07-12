package cz.palocko.directhp.data

sealed class LoadResult<out T> {
    data class Success<T> (val data: T): LoadResult<T>()
    data class Error(val error: Exception): LoadResult<Nothing>()
}
