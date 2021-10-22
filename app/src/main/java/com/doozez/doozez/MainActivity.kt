package com.doozez.doozez

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.databinding.ActivityMainBinding
import com.doozez.doozez.services.NotificationService
import com.doozez.doozez.utils.SharedPrerfKey
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navView.bringToFront()

        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            redirectToLogin()
            false
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_invitation, R.id.nav_safe, R.id.nav_payment_methods, R.id.nav_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        NotificationService.registerChannels(this)
        NotificationService.registerDevice()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    private fun checkAuthentication() {
//        val apiKey = SharedPrefManager.getString(SharedPrerfKey.API_KEY, null, true)
//        if (apiKey.isNullOrEmpty()) {
//            redirectToLogin()
//        }
//    }

    private fun redirectToLogin() {
        SharedPrefManager.clear()
        Intent(this@MainActivity, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { startActivity(it) }
        finish()
    }

    fun triggerOverlay() {
        var visibility = View.GONE
        if (binding.appBarMain.contentMain.overlayLoader.progressView.visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        binding.appBarMain.contentMain.overlayLoader.progressView.visibility = visibility
    }
}