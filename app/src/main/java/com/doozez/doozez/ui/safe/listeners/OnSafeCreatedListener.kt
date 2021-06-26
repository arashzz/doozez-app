package com.doozez.doozez.ui.safe.listeners

interface OnSafeCreatedListener {
    fun onSuccessSafeCreate(msg: String)
    fun onFailureSafeCreate(msg: String)
}