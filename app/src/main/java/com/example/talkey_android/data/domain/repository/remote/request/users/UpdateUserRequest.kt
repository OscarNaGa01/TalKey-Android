package com.example.talkey_android.data.domain.repository.remote.request.users

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("password")
    val password: String?,
    @SerializedName("nick")
    val nick: String?
)
