package com.example.talkey_android.data.domain.repository.remote.request.users

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("password")
    val password: String?,
    @SerializedName("nick")
    val nick: String?
)
