package com.doozez.doozez.utils

import android.content.Context

object Utils {
    fun isInteger(str: String?) = str?.toIntOrNull()?.let { true } ?: false
}