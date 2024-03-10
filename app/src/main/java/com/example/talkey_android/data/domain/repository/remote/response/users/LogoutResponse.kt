package com.example.talkey_android.data.domain.repository.remote.response.users

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("message")
    val message: String?
)