package com.example.talkey_android.data.domain.use_cases.messages

import com.example.talkey_android.data.domain.model.messages.ListMessageModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class GetListMessageUseCase {
    suspend operator fun invoke(
        idChat: String,
        limit: Int,
        offset: Int
    ): BaseResponse<ListMessageModel> {
        return DataProvider.getMessages(idChat, limit, offset)
    }
}