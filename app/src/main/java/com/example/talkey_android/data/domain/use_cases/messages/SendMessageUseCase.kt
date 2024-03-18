package com.example.talkey_android.data.domain.use_cases.messages

import com.example.talkey_android.data.domain.model.common.SuccessModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class SendMessageUseCase {
    suspend operator fun invoke(
        token: String,
        chat: String,
        source: String,
        message: String
    ): BaseResponse<SuccessModel> {
        return DataProvider.sendMessage(token, chat, source, message)
    }
}