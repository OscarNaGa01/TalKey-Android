package com.example.talkey_android.data.domain.repository.remote.mapper.chats

import com.example.talkey_android.data.domain.model.chats.ChatBasicInfoModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.chats.ChatBasicInfoResponse

class ChatBasicInfoMapper : ResponseMapper<ChatBasicInfoResponse, ChatBasicInfoModel> {
    override fun fromResponse(response: ChatBasicInfoResponse): ChatBasicInfoModel {
        return ChatBasicInfoModel(
            response.created ?: "",
            response.id ?: "",
            response.source ?: "",
            response.target ?: ""
        )
    }
}