package com.example.talkey_android.data.domain.use_cases.chats

import com.example.talkey_android.data.domain.model.chats.ListChatsModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class GetListChatsUseCase {
    suspend operator fun invoke(): BaseResponse<ListChatsModel> {
        return DataProvider.getListChats()
    }
}