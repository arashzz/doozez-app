package com.doozez.doozez

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.auth.LoginCreateReq
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.databinding.ActivityLoginBinding
import com.doozez.doozez.utils.BundleKey
import com.doozez.doozez.utils.SharedPrerfKey
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra(BundleKey.EMAIL)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!email.isNullOrEmpty()) {
            binding.loginEmail.editText?.setText(email)
        }
        registerFCM()
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
        binding.loginRegister.setOnClickListener {
            navigateToActivity(RegisterActivity::class.java)
        }
    }

    private fun validateForm(): Boolean {
        return binding.loginEmail.error == null &&
                binding.loginPassword.error == null
    }

    private fun <T> navigateToActivity(cls: Class<T>) {
        Intent(this@LoginActivity, cls).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { startActivity(it) }
        finish()
    }

    private fun login(body: LoginCreateReq) {
        val call = ApiClient.authService.login(body)
        triggerOverlay()
        call.enqueue {
            onResponse = {
                if(it.isSuccessful && it.body() != null) {
                    SharedPrefManager.putString(SharedPrerfKey.API_KEY, it.body().apiKey, true)
                    getUserForToken(it.body().apiKey)
                } else {
                    Snackbar.make(
                        binding.loginContainer,
                        "Login failed. Invalid Credentials",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            onFailure = {
                triggerOverlay()
                Log.e("LoginActivity", it?.stackTrace.toString())
                Snackbar.make(
                    binding.loginContainer,
                    "failed to login",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getUserForToken(apiToken: String) {
        val call = ApiClient.authService.validateToken()
        call.enqueue {
            onResponse = {
                if(it.isSuccessful && it.body() != null) {
                    SharedPrefManager.putUser(it.body())
                    triggerOverlay()
                    navigateToActivity(MainActivity::class.java)
                }
            }
            onFailure = {
                triggerOverlay()
                Log.e("loginContainer", it?.stackTrace.toString())
                Snackbar.make(
                    binding.loginContainer,
                    "Unknown Error",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun triggerOverlay() {
        var visibility = View.GONE
        if (binding.overlayLoader.progressView.visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        binding.overlayLoader.progressView.visibility = visibility
    }

    private fun registerFCM() {
        //TODO: move out
        val name = getString(R.string.action_add)
        val descriptionText = getString(R.string.action_add)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("1", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


        //TODO: move out
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = token
            Log.d("TAG", token!!)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

}