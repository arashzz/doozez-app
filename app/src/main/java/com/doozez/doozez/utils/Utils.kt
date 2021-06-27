package com.doozez.doozez.utils

object Utils {
    fun isInteger(str: String?) = str?.toIntOrNull()?.let { true } ?: false
}