package com.example.talkey_android.data.domain.model.users

import com.example.talkey_android.data.domain.model.BaseModel

data class LoginRequestModel(
    val password: String = "",
    val login: String = "",
    var platform: String = "",
    var firebaseToken: String = ""
) : BaseModel()
