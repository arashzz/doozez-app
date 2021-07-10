package com.doozez.doozez

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.doozez.doozez.databinding.ActivityLoginBinding
import com.doozez.doozez.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun addListeners() {

    }
}