package com.example.talkey_android.data.domain.model.chats

import com.example.talkey_android.data.domain.model.BaseModel

data class ChatCreationToRequestModel(
    val source: String = "",
    val target: String = ""
) : BaseModel()
