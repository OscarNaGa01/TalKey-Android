package com.example.talkey_android.data.domain.use_cases.users

import com.example.talkey_android.data.domain.model.users.UserFullDataModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class GetProfileUseCase {
    suspend operator fun invoke(token: String): BaseResponse<UserFullDataModel> {
        return DataProvider.getProfile(token)
    }
}