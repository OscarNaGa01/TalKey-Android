package com.example.talkey_android.data.domain.repository.remote.response.users

import com.google.gson.annotations.SerializedName

data class UserFromLoginResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("nick")
    val nick: String?,
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("online")
    val online: Boolean?
)
