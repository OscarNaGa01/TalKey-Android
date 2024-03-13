package com.example.talkey_android.data.domain.repository.remote.mapper.chats

import com.example.talkey_android.data.domain.model.chats.ChatCreationToRequestModel
import com.example.talkey_android.data.domain.repository.remote.mapper.RequestMapper
import com.example.talkey_android.data.domain.repository.remote.request.chats.ChatCreationRequest

class ChatCreationToRequestMapper : RequestMapper<ChatCreationToRequestModel, ChatCreationRequest> {
    override fun toRequest(model: ChatCreationToRequestModel): ChatCreationRequest {
        return ChatCreationRequest(
            model.source,
            model.target
        )
    }
}