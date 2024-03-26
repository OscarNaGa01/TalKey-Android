package com.example.talkey_android.data

data object Token {

    private var token: String = ""

    fun setToken(newToken: String) {
        token = newToken
    }

    fun getToken(): String = token
}


