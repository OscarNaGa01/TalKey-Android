package com.example.talkey_android.data.utils

import android.content.Context

class Prefs(val context: Context) {

    val SHARED_DB_NAME = "db"
    val SHARED_TOKEN = "token"
    val storage = context.getSharedPreferences(SHARED_DB_NAME, 0)

    fun saveToken(token: String) {
        storage.edit().putString(SHARED_TOKEN, token).apply()
    }

    fun getToken(): String {
        return storage.getString(SHARED_TOKEN, "")!!
    }
}