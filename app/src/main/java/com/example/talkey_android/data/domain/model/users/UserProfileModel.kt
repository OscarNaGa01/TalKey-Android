package com.example.talkey_android.data.domain.model.users

import com.example.talkey_android.data.domain.model.BaseModel


data class UserProfileModel(
    val id: String = "",
    val login: String = "",
    val nick: String = "",
    val avatar: String = "",
    val token: String = "",
    val online: Boolean = true,
    val password: String = ""
) : BaseModel()
