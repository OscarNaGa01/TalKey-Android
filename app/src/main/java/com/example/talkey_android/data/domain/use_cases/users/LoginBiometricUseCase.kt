package com.example.talkey_android.data.domain.use_cases.users

import com.example.talkey_android.data.domain.model.users.UserModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class LoginBiometricUseCase {
    suspend operator fun invoke(): BaseResponse<UserModel> {
        return DataProvider.loginBiometric()
    }
}