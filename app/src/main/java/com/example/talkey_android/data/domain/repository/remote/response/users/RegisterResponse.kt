package com.example.talkey_android.data.domain.repository.remote.response.users

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("user")
    val user: UserFullDataResponse?
)
