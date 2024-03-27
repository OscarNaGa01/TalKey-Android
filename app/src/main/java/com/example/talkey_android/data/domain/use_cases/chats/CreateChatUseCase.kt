package com.example.talkey_android.data.domain.use_cases.chats

import com.example.talkey_android.data.domain.model.chats.ChatCreationModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class CreateChatUseCase {

    suspend operator fun invoke(
        source: String,
        target: String
    ): BaseResponse<ChatCreationModel> {
        return DataProvider.createChat(source, target)
    }
}