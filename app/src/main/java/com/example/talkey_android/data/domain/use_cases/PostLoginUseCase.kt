package com.example.talkey_android.data.domain.use_cases

import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.UserModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class PostLoginUseCase {
    suspend operator fun invoke(loginRequestModel: LoginRequestModel): BaseResponse<UserModel> {
        return DataProvider.postLogin(loginRequestModel)
    }
}