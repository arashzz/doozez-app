//package com.doozez.doozez.framework.presentation
//
//import com.doozez.doozez.business.domain.state.DialogInputCaptureCallback
//import com.doozez.doozez.business.domain.state.Response
//import com.doozez.doozez.business.domain.state.StateMessageCallback
//
//interface UIController {
//    fun displayProgressBar(isDisplayed: Boolean)
//
//    fun hideSoftKeyboard()
//
//    fun displayInputCaptureDialog(title: String, callback: DialogInputCaptureCallback)
//
//    fun onResponseReceived(
//        response: Response,
//        stateMessageCallback: StateMessageCallback
//    )
//
//}