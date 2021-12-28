package com.doozez.doozez.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doozez.doozez.Event
import com.doozez.doozez.MainActivity
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.auth.LoginCreateReq
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.enums.NavigationDirection
import com.doozez.doozez.enums.SharedPrerfKey
import com.doozez.doozez.utils.Common
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

class LoginViewModel: ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _emailError = MutableLiveData<Event<Int>>()
    val emailError: LiveData<Event<Int>> = _emailError

    private val _passwordError = MutableLiveData<Event<Int>>()
    val passwordError: LiveData<Event<Int>> = _passwordError

    private val _navigateTo = MutableLiveData<Event<NavigationDirection>>()
    val navigateTo: LiveData<Event<NavigationDirection>> = _navigateTo

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    fun register() {
        _navigateTo.value = Event(NavigationDirection.ACTIVITY_REGISTER)
    }

    fun login() {
        if(_isLoading.value == true) {
            return
        }
        if(validateLoginData()) {
            val body = LoginCreateReq(email.value!!, password.value!!)
            _isLoading.value = true
            viewModelScope.launch {
                val call = ApiClient.authService.login(body)
                call.enqueue {
                    onResponse = {
                        if(it.isSuccessful && it.body() != null) {
                            SharedPrefManager.putString(SharedPrerfKey.API_KEY.name, it.body().apiKey, true)
                            getUserForToken(it.body().apiKey)
                        } else {
                            _isLoading.value = false
                            _snackbarText.value = Event(R.string.login_invalid_credentials)
                        }
                    }
                    onFailure = {
                        _isLoading.value = false
                        _snackbarText.value = Event(R.string.login_error)
                    }
                }
            }
        }
    }

    private fun validateLoginData(): Boolean {
        var validationResult = true
        if(email.value.isNullOrBlank()) {
            _emailError.value = Event(R.string.error_validation_empty_email)
            validationResult = false
        } else if(!Common.isEmail(email.value!!)) {
            _emailError.value = Event(R.string.error_validation_invalid_email)
            validationResult = false
        }
        if(password.value.isNullOrBlank()) {
            _passwordError.value = Event(R.string.error_validation_empty_password)
            validationResult = false
        }
        return validationResult
    }

    private fun getUserForToken(apiToken: String) {
        val call = ApiClient.authService.validateToken()
        call.enqueue {
            onResponse = {
                if(it.isSuccessful && it.body() != null) {
                    SharedPrefManager.putUser(it.body())
                    _isLoading.value = false
                    _navigateTo.value = Event(NavigationDirection.ACTIVITY_MAIN)
                }
            }
            onFailure = {
                _isLoading.value = false
                Log.e("loginContainer", it?.stackTrace.toString())
                _snackbarText.value = Event(R.string.login_error)
            }
        }
    }
}