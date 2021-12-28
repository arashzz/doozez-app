package com.doozez.doozez.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

object Common {
    fun isInteger(str: String?) = str?.toIntOrNull()?.let { true } ?: false

    fun calculateDays(dateStr: String): Long {
        val now = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        val date = getDate(dateStr)

        return ChronoUnit.DAYS.between(date, now)
    }

    fun getDate(dateStr: String): LocalDateTime {
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        return LocalDateTime.parse(dateStr, dtf)
    }

    fun isEmail(str: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
}