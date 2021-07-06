package com.doozez.doozez.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun<T> Call<T>.enqueue(callback: DoozCallBack<T>.() -> Unit) {
    val callBackKt = DoozCallBack<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}

class DoozCallBack<T>: Callback<T> {

    var onResponse: ((Response<T>) -> Unit)? = null
    var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(response)
    }

}