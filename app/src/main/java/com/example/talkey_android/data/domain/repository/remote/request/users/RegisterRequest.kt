package com.example.talkey_android.data.domain.repository.remote.request.users

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("login")
    val login: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("nick")
    val nick: String?,
    @SerializedName("platform")
    val platform: String?,
    @SerializedName("firebaseToken")
    val firebaseToken: String?
)
