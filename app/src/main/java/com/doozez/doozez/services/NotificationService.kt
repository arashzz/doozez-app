package com.doozez.doozez.services

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.doozez.doozez.R
import com.doozez.doozez.utils.NotificationAction

object NotificationService {

    fun registerChannels(activity: Activity) {
        val notificationManager: NotificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        enumValues<com.doozez.doozez.utils.NotificationChannel>().forEach {
            val channel = NotificationChannel(
                it.name,
                it.channelName,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = it.description
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun pushNotificationForAction(activity: Activity, channel: com.doozez.doozez.utils.NotificationChannel) {
        val builder = NotificationCompat.Builder(activity, channel.name)
            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
            .setContentTitle("sample 1")
            .setContentText("sample 2")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(requireActivity())) {
            notify(123, builder.build())
        }
    }

    private fun getChannelForAction(action: NotificationAction) {
        if(action == NotificationAction.SAFE_WIN)
    }
}