package com.example.talkey_android.data.domain.use_cases.chats

import com.example.talkey_android.data.domain.model.common.SuccessModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class DeleteChatUseCase {
    suspend operator fun invoke(token: String, idChat: Int): BaseResponse<SuccessModel> {
        return DataProvider.deleteChat(token, idChat)
    }
}