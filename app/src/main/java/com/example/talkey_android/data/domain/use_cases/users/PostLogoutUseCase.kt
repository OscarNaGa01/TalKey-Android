package com.example.talkey_android.data.domain.use_cases.users

import com.example.talkey_android.data.domain.model.common.MessageModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class PostLogoutUseCase {
    suspend operator fun invoke(token: String): BaseResponse<MessageModel> {
        return DataProvider.postLogout(token)
    }
}