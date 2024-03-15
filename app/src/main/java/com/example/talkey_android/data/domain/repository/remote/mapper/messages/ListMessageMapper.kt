package com.example.talkey_android.data.domain.repository.remote.mapper.messages

import com.example.talkey_android.data.domain.model.messages.ListMessageModel
import com.example.talkey_android.data.domain.model.messages.MessageModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.messages.ListMessageResponse

class ListMessageMapper : ResponseMapper<ListMessageResponse, ListMessageModel> {
    override fun fromResponse(response: ListMessageResponse): ListMessageModel {
        val resultModel = arrayListOf<MessageModel>()
        if (!response.rows.isNullOrEmpty()) {
            val messageMapper = MessageMapper()
            response.rows.forEach {
                resultModel.add(messageMapper.fromResponse(it))
            }
        }
        return ListMessageModel(response.count ?: 0, resultModel)
    }
}