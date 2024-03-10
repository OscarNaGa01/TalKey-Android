package com.example.talkey_android.data.domain.use_cases.users

import com.example.talkey_android.data.domain.model.common.MessageModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class PutNotificationUseCase {
    suspend operator fun invoke(token: String, firebaseToken: String): BaseResponse<MessageModel> {
        return DataProvider.putNotification(token, firebaseToken)
    }
}