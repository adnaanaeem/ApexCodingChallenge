package com.apex.codeassesment.data.remote

sealed class ApiResult<out T> {
    data class Loading(val message: String) : ApiResult<Nothing>()
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()
}