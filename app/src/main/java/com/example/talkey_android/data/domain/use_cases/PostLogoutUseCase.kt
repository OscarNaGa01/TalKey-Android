package com.example.talkey_android.data.domain.use_cases

import com.example.talkey_android.data.domain.model.users.LogoutModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class PostLogoutUseCase {
    suspend operator fun invoke(token: String): BaseResponse<LogoutModel> {
        return DataProvider.postLogout(token)
    }
}