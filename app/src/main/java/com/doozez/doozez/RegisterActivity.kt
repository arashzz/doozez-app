package com.doozez.doozez

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.auth.RegisterCreateReq
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.databinding.ActivityRegisterBinding
import com.doozez.doozez.enums.BundleKey
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addListeners()
    }

    private fun addListeners() {
        binding.registerEmail.editText?.doOnTextChanged { inputText, _, _, _ ->
            if (inputText.isNullOrEmpty()) {
                binding.registerEmail.error = getString(R.string.error_validation_empty_email)
            } else {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputText).matches()) {
                    binding.registerEmail.error = getString(R.string.error_validation_invalid_email)
                } else {
                    binding.registerEmail.error = null
                }
            }
        }
        binding.registerFirstname.editText?.doOnTextChanged { inputText, _, _, _ ->
            if (inputText.isNullOrEmpty()) {
                binding.registerFirstname.error = getString(R.string.error_validation_empty_first_name)
            } else {
                binding.registerFirstname.error = null
            }
        }

        binding.registerLastname.editText?.doOnTextChanged { inputText, _, _, _ ->
            if (inputText.isNullOrEmpty()) {
                binding.registerLastname.error = getString(R.string.error_validation_empty_last_name)
            } else {
                binding.registerLastname.error = null
            }
        }

        binding.registerPassword.editText?.doOnTextChanged { inputText, _, _, _ ->
            if (inputText.isNullOrEmpty()) {
                binding.registerPassword.error = getString(R.string.error_validation_empty_password)
            } else if (!validatePasswords()){
                binding.registerPassword.error = getString(R.string.error_validation_not_matching_passwords)
            } else {
                binding.registerPassword.error = null
                binding.registerPasswordConfirm.error = null
            }
        }

        binding.registerPasswordConfirm.editText?.doOnTextChanged { inputText, _, _, _ ->
            if (inputText.isNullOrEmpty()) {
                binding.registerPasswordConfirm.error = getString(R.string.error_validation_empty_password)
            } else if (!validatePasswords()){
                binding.registerPasswordConfirm.error = getString(R.string.error_validation_not_matching_passwords)
            } else {
                binding.registerPasswordConfirm.error = null
                binding.registerPassword.error = null
            }
        }

        binding.registerRegister.setOnClickListener {
            if(validateForm()) {
                register(getRegistrationForm())
            } else {
                Snackbar.make(
                    binding.registerContainer,
                    "Wrong registration information provided",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.registerLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun register(body: RegisterCreateReq) {
        val call = ApiClient.authService.register(body)
        triggerOverlay()
        call.enqueue {
            onResponse = {
                if (it.isSuccessful) {
                    triggerOverlay()
                    AlertDialog.Builder(this@RegisterActivity)
                        .setTitle("Thank you ${body.firstName}")
                        .setMessage("Awesome! You will receive an activation email shortly!")
                        .setOnDismissListener {
                            navigateToLogin()
                        }
                        .setPositiveButton("Login") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                } else {
                    triggerOverlay()
                    Snackbar.make(
                        binding.registerContainer,
                        "failed to register, please try again",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            onFailure = {
                triggerOverlay()
                Log.e("RegisterActivity", it?.stackTrace.toString())
                Snackbar.make(
                    binding.registerContainer,
                    "failed to register",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getRegistrationForm(): RegisterCreateReq {
        return RegisterCreateReq(
            binding.registerEmail.editText?.text.toString(),
            binding.registerFirstname.editText?.text.toString(),
            binding.registerLastname.editText?.text.toString(),
            binding.registerPassword.editText?.text.toString(),
            binding.registerPasswordConfirm.editText?.text.toString()
        )
    }

    private fun triggerOverlay() {
        var visibility = View.GONE
        if (binding.overlayLoader.progressView.visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        binding.overlayLoader.progressView.visibility = visibility
    }

    private fun navigateToLogin() {
        Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { startActivity(it) }
        finish()
    }

    private fun validatePasswords(): Boolean {
        val pwd = binding.registerPassword.editText?.text.toString()
        val pwdConfirm = binding.registerPasswordConfirm.editText?.text.toString()
        return pwd == pwdConfirm
    }

    private fun validateForm(): Boolean {
        return binding.registerEmail.error == null &&
                binding.registerFirstname.error == null &&
                binding.registerLastname.error == null &&
                binding.registerPassword.error == null &&
                binding.registerPasswordConfirm.error == null &&
                binding.registerTerms.isChecked
    }
}