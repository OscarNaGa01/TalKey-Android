package com.example.talkey_android.data.utils

import android.content.Context

class Prefs(val context: Context) {

    val SHARED_DB_NAME = "db"
    val SHARED_TOKEN = "token"
    val SHARED_MAIL = "mail"
    val storage = context.getSharedPreferences(SHARED_DB_NAME, 0)

    fun saveToken(token: String) {
        storage.edit().putString(SHARED_TOKEN, token).apply()
    }

    fun getToken(): String {
        return storage.getString(SHARED_TOKEN, "")!!
    }

    fun saveMail(mail: String) {
        storage.edit().putString(SHARED_MAIL, mail).apply()
    }

    fun getMail(): String {
        return storage.getString(SHARED_MAIL, "")!!
    }

    fun restartData() {
        storage.edit().putString(SHARED_MAIL, "").apply()
        storage.edit().putString(SHARED_TOKEN, "").apply()
    }
}