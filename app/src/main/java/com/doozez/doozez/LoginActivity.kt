package com.doozez.doozez

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.auth.LoginCreateReq
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.databinding.ActivityLoginBinding
import com.doozez.doozez.services.NotificationService
import com.doozez.doozez.enums.BundleKey
import com.doozez.doozez.enums.NavigationDirection
import com.doozez.doozez.enums.SharedPrerfKey
import com.doozez.doozez.utils.setupSnackbar
import com.doozez.doozez.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel

        registerFCM()
        setupSnackbar()
        setupSubscriptions()
    }

    private fun setupSubscriptions() {
        viewModel.emailError.observe(this, EventObserver {
            binding.loginEmail.error = getString(it)
        })
        viewModel.passwordError.observe(this, EventObserver {
            binding.loginPassword.error = getString(it)
        })
        viewModel.navigateTo.observe(this, EventObserver {
            when(it) {
                NavigationDirection.ACTIVITY_MAIN -> navigateToActivity(MainActivity::class.java)
                NavigationDirection.ACTIVITY_REGISTER -> navigateToActivity(RegisterActivity::class.java)
            }
        })
        viewModel.isLoading.observe(this, Observer {
            if(it) {
                binding.overlayLoader.progressView.visibility = View.VISIBLE
            } else {
                binding.overlayLoader.progressView.visibility = View.GONE
            }
        })
    }

    private fun setupSnackbar() {
        binding.loginContainer.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun <T> navigateToActivity(cls: Class<T>) {
        Intent(this@LoginActivity, cls).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { startActivity(it) }
        finish()
    }

    private fun registerFCM() {
        NotificationService.registerChannels(this)
    }

}