package com.example.talkey_android.data.domain.repository.remote.mapper.messages

import com.example.talkey_android.data.domain.model.messages.MessageModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.messages.MessageResponse
import com.example.talkey_android.data.utils.Utils

class MessageMapper : ResponseMapper<MessageResponse, MessageModel> {
    override fun fromResponse(response: MessageResponse): MessageModel {
        var formattedDay = ""
        var formattedHour = ""

        if (response.date != null) {
            formattedDay = Utils.formatDate(response.date).substring(0, 10)
            formattedHour = Utils.formatDate(response.date).substring(11, 16)
        }

        return MessageModel(
            response.id ?: "",
            response.chat ?: "",
            response.source ?: "",
            response.message ?: "",
            response.date ?: "",
            formattedDay,
            formattedHour
        )
    }
}