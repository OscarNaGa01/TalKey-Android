package com.example.talkey_android.data.domain.repository.remote.mapper.chats

import com.example.talkey_android.data.domain.model.chats.ChatModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.chats.ChatResponse

class ChatMapper : ResponseMapper<ChatResponse, ChatModel> {
    override fun fromResponse(response: ChatResponse): ChatModel {
        return ChatModel(
            response.idChat ?: "",
            response.source ?: "",
            response.sourceNick ?: "",
            response.sourceAvatar ?: "",
            response.sourceOnline ?: false,
            response.sourceToken ?: "",
            response.target ?: "",
            response.targetNick ?: "",
            response.targetAvatar ?: "",
            response.targetOnline ?: false,
            response.targetToken ?: "",
            response.chatCreated ?: ""
        )
    }
}