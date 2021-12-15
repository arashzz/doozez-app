package com.doozez.doozez.enums

enum class NotificationChannel(val channelName: String, val description: String) {
    SAFE("Safe", "Safe related notifications"),
    GENERAL("General", "General notifications"),
    PAYMENT("Payment", "Payment related notifications");
}