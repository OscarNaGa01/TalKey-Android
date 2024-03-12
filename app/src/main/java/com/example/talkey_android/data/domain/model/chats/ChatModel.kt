package com.example.talkey_android.data.domain.model.chats

import com.example.talkey_android.data.domain.model.BaseModel

data class ChatModel(
    val id: String = "",
    val source: String = "",
    val target: String = "",
    val created: String = "",
) : BaseModel()
