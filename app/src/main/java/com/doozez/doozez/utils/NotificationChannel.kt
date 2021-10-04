package com.doozez.doozez.utils

enum class NotificationChannel(val channelName: String, val description: String) {
    SAFE("Safe", "Safe related notifications"),
    GENERAL("General", "General notifications"),
    PAYMENT("Payment", "Payment related notifications");
}