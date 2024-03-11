package com.example.talkey_android.data.domain.repository.remote.response.users

import com.google.gson.annotations.SerializedName

data class UserFullDataResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("login")
    val login: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("nick")
    val nick: String?,
    @SerializedName("platform")
    val platform: String?,
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("uuid")
    val uuid: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("online")
    val online: Boolean?,
    @SerializedName("created")
    val created: String?,
    @SerializedName("updated")
    val updated: String?
)
