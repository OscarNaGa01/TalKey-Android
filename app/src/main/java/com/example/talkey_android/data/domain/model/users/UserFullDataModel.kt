package com.example.talkey_android.data.domain.model.users


data class UserFullDataModel(
    val id: String = "",
    val login: String = "",
    val password: String = "",
    val nick: String = "",
    val platform: String = "",
    val avatar: String = "",
    val uuid: String = "",
    val token: String = "",
    val online: Boolean = false,
    val created: String = "",
    val updated: String = ""
)
