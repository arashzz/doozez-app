package com.doozez.doozez.services

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.SharedPrefManager
import com.doozez.doozez.api.device.DeviceCreateReq
import com.doozez.doozez.api.enqueue
import com.doozez.doozez.enums.SharedPrerfKey
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

object NotificationService {

    private const val TAG: String = "NotificationService"

    fun registerChannels(activity: Activity) {
        val notificationManager: NotificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        enumValues<com.doozez.doozez.enums.NotificationChannel>().forEach {
            val channel = NotificationChannel(
                it.name,
                it.channelName,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = it.description
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun registerDevice() {
        val currentToken = SharedPrefManager.getString(SharedPrerfKey.FCM_REGISTRATION_ID.name)
        if(currentToken.isNullOrBlank()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(TAG, task.exception.toString())
                    return@OnCompleteListener
                }
                val token = task.result
                Log.i(TAG, "Successfully registered the device with FCM with the token value [$token]")
                sendRegistrationToServer(token)
            })
        }
    }

    fun sendRegistrationToServer(token: String?) {
        val userID = SharedPrefManager.getInt(SharedPrerfKey.USER_ID.name)
        val body = DeviceCreateReq(token!!, userID)
        val call = ApiClient.deviceService.register(body)
        call.enqueue {
            onResponse = {
                if(it.isSuccessful) {
                    Log.i(TAG, "Successfully registered device with FCM for user [$userID] and registration ID [$token]")
                    SharedPrefManager.putString(SharedPrerfKey.FCM_REGISTRATION_ID.name, token)
                } else {
                    Log.i(TAG, "Failed to register device with FCM for user [$userID] and registration ID [$token] and response message ${it.message()}")
                }
            }

            onFailure = {
                Log.e(TAG, it?.stackTraceToString()!!)
            }
        }
    }

//    fun pushNotificationForAction(activity: Activity, channel: com.doozez.doozez.enums.NotificationChannel) {
//        val builder = NotificationCompat.Builder(activity, channel.name)
//            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
//            .setContentTitle("sample 1")
//            .setContentText("sample 2")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        with(NotificationManagerCompat.from(activity)) {
//            notify(123, builder.build())
//        }
//    }

//    private fun getChannelForAction(action: NotificationAction) {
//        if(action == NotificationAction.SAFE_WIN)
//    }

}