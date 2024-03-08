package com.example.talkey_android.data.domain.model.error

import com.example.talkey_android.data.domain.model.BaseModel

data class ErrorModel(
    var error: String = "Unknown",
    var errorCode: String = "",
    var message: String = "Unknown"
) : BaseModel()