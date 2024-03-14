package com.example.talkey_android.data.domain.repository.remote.mapper.chats

import com.example.talkey_android.data.domain.model.chats.ChatModel
import com.example.talkey_android.data.domain.model.chats.ListChatsModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.chats.ChatResponse

class ListChatsMapper : ResponseMapper<List<ChatResponse>, ListChatsModel> {
    override fun fromResponse(response: List<ChatResponse>): ListChatsModel {
        val resultModel = mutableListOf<ChatModel>()
        if (response.isNotEmpty()) {
            val chatMapper = ChatMapper()
            response.forEach {
                resultModel.add(chatMapper.fromResponse(it))
            }
        }
        return ListChatsModel(resultModel)
    }
}