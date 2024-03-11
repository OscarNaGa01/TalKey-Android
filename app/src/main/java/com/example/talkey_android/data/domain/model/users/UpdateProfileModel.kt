package com.example.talkey_android.data.domain.model.users

import com.example.talkey_android.data.domain.model.BaseModel

data class UpdateProfileModel(
    val password: String = "",
    val nick: String = ""
) : BaseModel()
