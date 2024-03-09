package com.example.talkey_android.data.domain.model.users

import com.example.talkey_android.data.domain.model.BaseModel

data class LoginToRequestModel(
    val password: String = "",
    val login: String = "",
    val platform: String = "",
    val firebaseToken: String = ""
) : BaseModel()
