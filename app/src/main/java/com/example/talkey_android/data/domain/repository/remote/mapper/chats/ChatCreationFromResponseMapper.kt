package com.example.talkey_android.data.domain.repository.remote.mapper.chats

import com.example.talkey_android.data.domain.model.chats.ChatBasicInfoModel
import com.example.talkey_android.data.domain.model.chats.ChatCreationFromResponseModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.chats.ChatCreationResponse

class ChatCreationFromResponseMapper :
    ResponseMapper<ChatCreationResponse, ChatCreationFromResponseModel> {
    override fun fromResponse(response: ChatCreationResponse): ChatCreationFromResponseModel {
        return ChatCreationFromResponseModel(
            response.success ?: false,
            response.created ?: false,
            createBasicInfoModel(response)
        )
    }

    private fun createBasicInfoModel(response: ChatCreationResponse): ChatBasicInfoModel {
        var chatBasicInfoModel = ChatBasicInfoModel()
        if (response.chat != null) {
            chatBasicInfoModel = ChatBasicInfoMapper().fromResponse(response.chat)
        }
        return chatBasicInfoModel
    }
}