package com.example.talkey_android.data.domain.model.chats

import com.example.talkey_android.data.domain.model.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatItemListModel(
    val idChat: String = "",
    val idUser: String = "",
    val contactNick: String = "",
    val contactOnline: Boolean = false,
    val contactAvatar: String = "",
    var lastMessage: String = "",
    var dateLastMessage: String = ""
) : BaseModel()