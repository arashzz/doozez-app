package com.doozez.doozez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.auth.LoginCreateReq
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.databinding.ActivityLoginBinding
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.SharedPrerfKey
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = SharedPrefManager.get(SharedPrerfKey.API_KEY, null, true)
        if(!apiKey.isNullOrBlank()) {
            navigateToMain()
        }

        val email = intent.getStringExtra(BundleKey.EMAIL)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!email.isNullOrEmpty()) {
            binding.loginEmail.editText?.setText(email)
        }
        addListeners()
    }

    private fun addListeners() {
        binding.loginLogin.setOnClickListener {
            if (validateForm()) {
                login(LoginCreateReq(
                    binding.loginEmail.editText?.text.toString(),
                    binding.loginPassword.editText?.text.toString()
                ))
            } else {
                Snackbar.make(
                    binding.loginContainer,
                    "Incorrect credentials",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        binding.loginEmail.editText?.doOnTextChanged { inputText, _, _, _ ->
            if (inputText.isNullOrEmpty()) {
                binding.loginEmail.error = getString(R.string.error_validation_empty_email)
            } else {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputText).matches()) {
                    binding.loginEmail.error = getString(R.string.error_validation_invalid_email)
                } else {
                    binding.loginEmail.error = null
                }
            }
        }
        binding.loginPassword.editText?.doOnTextChanged { inputText, _, _, _ ->
            if (inputText.isNullOrEmpty()) {
                binding.loginPassword.error = getString(R.string.error_validation_empty_password)
            } else {
                binding.loginPassword.error = null
            }
        }
    }

    private fun validateForm(): Boolean {
        return binding.loginEmail.error == null &&
                binding.loginPassword.error == null
    }

    private fun navigateToMain() {
        Intent(this@LoginActivity, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { startActivity(it) }
        finish()
    }

    private fun login(body: LoginCreateReq) {
        val call = ApiClient.authService.login(body)
        call.enqueue {
            onResponse = {
                if(it.isSuccessful && it.body() != null) {
                    SharedPrefManager.put(SharedPrerfKey.API_KEY, it.body().apiKey, true)
                    navigateToMain()
                } else {
                    Snackbar.make(
                        binding.loginContainer,
                        "Login failed. Invalid Credentials",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            onFailure = {

            }
        }
    }
}