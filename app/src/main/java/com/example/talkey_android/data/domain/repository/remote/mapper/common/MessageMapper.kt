package com.example.talkey_android.data.domain.repository.remote.mapper.common

import com.example.talkey_android.data.domain.model.common.MessageModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.common.MessageResponse

class MessageMapper : ResponseMapper<MessageResponse, MessageModel> {
    override fun fromResponse(response: MessageResponse): MessageModel {
        return MessageModel(response.message ?: "")
    }
}