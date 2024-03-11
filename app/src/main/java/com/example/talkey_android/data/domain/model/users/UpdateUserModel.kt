package com.example.talkey_android.data.domain.model.users

import com.example.talkey_android.data.domain.model.BaseModel

data class UpdateUserModel(
    val password: String = "",
    val nick: String = ""
) : BaseModel()
