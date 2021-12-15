package com.doozez.doozez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.enums.SharedPrerfKey

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler(Looper.getMainLooper()).postDelayed({
            checkStuff()
        }, 1000)
    }

    private fun checkStuff() {
        val apiKey = SharedPrefManager.getString(SharedPrerfKey.API_KEY.name, null, true)
        val intentLogin = Intent(this, LoginActivity::class.java)
        val intentMain = Intent(this, MainActivity::class.java)
        if(!apiKey.isNullOrBlank()) {
            val call = ApiClient.authService.validateToken()
            call.enqueue {
                onResponse = {
                    if(it.isSuccessful && it.body() != null) {
                        SharedPrefManager.putUser(it.body())
                        loadActivity(intentMain, false)
                    } else {
                        loadActivity(intentLogin, true)
                    }
                }
                onFailure = {
                    loadActivity(intentLogin, true)

                }
            }
        } else {
            loadActivity(intentLogin, true)
        }
    }

    private fun loadActivity(intent: Intent, clearSharedPref: Boolean) {
        if(clearSharedPref) {
            SharedPrefManager.clear()
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}