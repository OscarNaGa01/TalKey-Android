package com.example.talkey_android.data.domain.use_cases.users

import com.example.talkey_android.data.domain.model.common.CommonMessageModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class LogoutUseCase {
    suspend operator fun invoke(token: String): BaseResponse<CommonMessageModel> {
        return DataProvider.logout(token)
    }
}