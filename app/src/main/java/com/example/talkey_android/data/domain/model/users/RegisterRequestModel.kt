package com.example.talkey_android.data.domain.model.users

import com.example.talkey_android.data.domain.model.BaseModel

data class RegisterRequestModel(
    val login: String = "",
    val password: String = "",
    val nick: String = "",
    val platform: String = "",
    val firebaseToken: String = ""
) : BaseModel()