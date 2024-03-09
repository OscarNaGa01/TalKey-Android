package com.example.talkey_android.data.domain.model.users

import com.example.talkey_android.data.domain.model.BaseModel

data class UserModel(
    val id: String = "",
    val nick: String = "",
    val avatar: String = "",
    val online: String = "",
    val token: String = ""
) : BaseModel()
