package com.example.talkey_android.data.domain.use_cases.chats

import com.example.talkey_android.data.domain.model.chats.ChatCreationFromResponseModel
import com.example.talkey_android.data.domain.model.chats.ChatCreationToRequestModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class CreateChatUseCase {

    suspend operator fun invoke(
        token: String,
        chatCreationToRequestModel: ChatCreationToRequestModel
    ): BaseResponse<ChatCreationFromResponseModel> {
        return DataProvider.createChat(token, chatCreationToRequestModel)
    }
}