package com.example.talkey_android.data.domain.repository.remote.mapper.messages

import com.example.talkey_android.data.domain.model.messages.MessageModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.messages.MessageResponse

class MessageMapper : ResponseMapper<MessageResponse, MessageModel> {
    override fun fromResponse(response: MessageResponse): MessageModel {
        return MessageModel(
            response.id ?: "",
            response.chat ?: "",
            response.source ?: "",
            response.message ?: "",
            response.date ?: ""
        )
    }
}