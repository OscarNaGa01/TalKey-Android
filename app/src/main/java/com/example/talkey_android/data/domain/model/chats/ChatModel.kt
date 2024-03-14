package com.example.talkey_android.data.domain.model.chats

import com.example.talkey_android.data.domain.model.BaseModel

data class ChatModel(
    val idChat: String = "",
    val source: String = "",
    val sourceNick: String = "",
    val sourceAvatar: String = "",
    val sourceOnline: Boolean = false,
    val sourceToken: String = "",
    val target: String = "",
    val targetNick: String = "",
    val targetAvatar: String = "",
    val targetOnline: Boolean = false,
    val targetToken: String = "",
    val chatCreated: String = ""
) : BaseModel()
