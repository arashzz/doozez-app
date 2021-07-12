package com.doozez.doozez.api

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.doozez.doozez.MyApplication

object SharedPrefManager {

    private const val PREFS_NAME = "prefs_name"
    private const val ENCRYPTED_PREFS_NAME = "encrypted_$PREFS_NAME"

    private val sharedPrefs by lazy {
        MyApplication.instance.applicationContext.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val encryptedSharedPrefs by lazy {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            ENCRYPTED_PREFS_NAME,
            masterKeyAlias,
            MyApplication.instance.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun put(key: String, value: String, encrypted: Boolean = false) {
        val prefs = if (!encrypted) sharedPrefs else encryptedSharedPrefs
        with(prefs.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun get(key: String, default: String? = null, encrypted: Boolean = false): String? {
        val prefs = if (!encrypted) sharedPrefs else encryptedSharedPrefs
        return prefs.getString(key, default)
    }

    fun clear() {
        sharedPrefs.edit().clear().apply()
        encryptedSharedPrefs.edit().clear().apply()
    }
}