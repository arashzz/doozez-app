//package com.doozez.doozez.business.data.network
//
//sealed class _ApiResult<out T> {
//
//    data class Success<out T>(val value: T): _ApiResult<T>()
//
//    data class GenericError(
//        val code: Int? = null,
//        val errorMessage: String? = null
//    ): _ApiResult<Nothing>()
//
//    object NetworkError: _ApiResult<Nothing>()
//}