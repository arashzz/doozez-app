package com.doozez.doozez.api

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.doozez.doozez.MyApplication
import com.doozez.doozez.api.user.UserDetailResp
import com.doozez.doozez.enums.SharedPrerfKey

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

    fun putString(key: String, value: String, encrypted: Boolean = false) {
        val prefs = if (!encrypted) sharedPrefs else encryptedSharedPrefs
        with(prefs.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun getString(key: String, default: String? = null, encrypted: Boolean = false): String? {
        val prefs = if (!encrypted) sharedPrefs else encryptedSharedPrefs
        return prefs.getString(key, default)
    }

    fun putInt(key: String, value: Int, encrypted: Boolean = false) {
        val prefs = if (!encrypted) sharedPrefs else encryptedSharedPrefs
        with(prefs.edit()) {
            putInt(key, value)
            commit()
        }
    }

    fun getInt(key: String, default: Int = 0, encrypted: Boolean = false): Int {
        val prefs = if (!encrypted) sharedPrefs else encryptedSharedPrefs
        return prefs.getInt(key, default)
    }

    fun putUser(user: UserDetailResp) {
        with(sharedPrefs.edit()) {
            putInt(SharedPrerfKey.USER_ID.name, user.id)
            putString(SharedPrerfKey.EMAIL.name, user.email)
            putString(SharedPrerfKey.FIRST_NAME.name, user.firstName)
            putString(SharedPrerfKey.LAST_NAME.name, user.lastName)
            commit()
        }
    }

    fun clear() {
        sharedPrefs.edit().clear().apply()
        encryptedSharedPrefs.edit().clear().apply()
    }
}