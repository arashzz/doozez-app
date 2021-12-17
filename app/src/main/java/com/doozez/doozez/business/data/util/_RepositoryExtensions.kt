//package com.doozez.doozez.business.data.util
//
//import com.doozez.doozez.business.data.network._ApiResult
//import com.doozez.doozez.business.data.network._NetworkConstants.NETWORK_TIMEOUT
//import com.doozez.doozez.business.data.network.NetworkErrors.NETWORK_ERROR_TIMEOUT
//import com.doozez.doozez.business.data.network.NetworkErrors.NETWORK_ERROR_UNKNOWN
//import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.TimeoutCancellationException
//import kotlinx.coroutines.withContext
//import kotlinx.coroutines.withTimeout
//import java.io.IOException
//
//suspend fun <T> safeApiCall(
//    dispatcher: CoroutineDispatcher,
//    apiCall: suspend () -> T?
//): _ApiResult<T?> {
//    return withContext(dispatcher) {
//        try {
//            // throws TimeoutCancellationException
//            withTimeout(NETWORK_TIMEOUT){
//                _ApiResult.Success(apiCall.invoke())
//            }
//        } catch (throwable: Throwable) {
//            throwable.printStackTrace()
//            when (throwable) {
//                is TimeoutCancellationException -> {
//                    val code = 408 // timeout error code
//                    _ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
//                }
//                is IOException -> {
//                    _ApiResult.NetworkError
//                }
//                is HttpException -> {
//                    val code = throwable.code()
//                    val errorResponse = convertErrorBody(throwable)
//                    cLog(errorResponse)
//                    _ApiResult.GenericError(
//                        code,
//                        errorResponse
//                    )
//                }
//                else -> {
//                    cLog(NETWORK_ERROR_UNKNOWN)
//                    _ApiResult.GenericError(
//                        null,
//                        NETWORK_ERROR_UNKNOWN
//                    )
//                }
//            }
//        }
//    }
//}
//
////suspend fun <T> safeCacheCall(
////    dispatcher: CoroutineDispatcher,
////    cacheCall: suspend () -> T?
////): CacheResult<T?> {
////    return withContext(dispatcher) {
////        try {
////            // throws TimeoutCancellationException
////            withTimeout(CACHE_TIMEOUT){
////                CacheResult.Success(cacheCall.invoke())
////            }
////        } catch (throwable: Throwable) {
////            throwable.printStackTrace()
////            when (throwable) {
////
////                is TimeoutCancellationException -> {
////                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
////                }
////                else -> {
////                    cLog(CACHE_ERROR_UNKNOWN)
////                    CacheResult.GenericError(CACHE_ERROR_UNKNOWN)
////                }
////            }
////        }
////    }
////}
//
//
//private fun convertErrorBody(throwable: HttpException): String? {
//    return try {
//        throwable.response()?.errorBody()?.string()
//    } catch (exception: Exception) {
//        ERROR_UNKNOWN
//    }
//}