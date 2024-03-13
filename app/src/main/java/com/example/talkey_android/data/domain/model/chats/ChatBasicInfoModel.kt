package com.example.talkey_android.data.domain.model.chats

import com.example.talkey_android.data.domain.model.BaseModel

data class ChatBasicInfoModel(
    val created: String = "",
    val id: String = "",
    val source: String = "",
    val target: String = ""
) : BaseModel()
