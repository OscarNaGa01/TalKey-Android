package com.example.talkey_android.data.domain.use_cases.users

import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterResponseModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class RegisterUseCase {
    suspend operator fun invoke(registerRequestModel: RegisterRequestModel): BaseResponse<RegisterResponseModel> {
        return DataProvider.register(registerRequestModel)
    }
}