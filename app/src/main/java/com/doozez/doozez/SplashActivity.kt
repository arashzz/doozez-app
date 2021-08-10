package com.doozez.doozez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.utils.SharedPrerfKey

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
        }, 2000)
    }

    private fun checkStuff() {
        val apiKey = SharedPrefManager.getString(SharedPrerfKey.API_KEY, null, true)
        var intent = Intent(this, LoginActivity::class.java)
        if(!apiKey.isNullOrBlank()) {
            intent = Intent(this, MainActivity::class.java)
        }
        intent.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { startActivity(it) }
        finish()
    }
}