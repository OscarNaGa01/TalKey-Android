package com.example.talkey_android.data.domain.model.messages

import com.example.talkey_android.data.domain.model.BaseModel

data class MessageModel(
    val id: String = "",
    val chat: String = "",
    val source: String = "",
    val message: String = "",
    var date: String = "",
    var day: String = "",
    val hour: String = ""
) : BaseModel()
