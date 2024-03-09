package com.example.talkey_android.data.domain.repository.remote.request.users

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("password")
    val password: String?,
    @SerializedName("login")
    val login: String?,
    @SerializedName("platform")
    val platform: String?,
    @SerializedName("firebaseToken")
    val firebaseToken: String?
)
