package com.example.talkey_android.data.domain.use_cases

import com.example.talkey_android.data.domain.model.users.RegisterFromResponseModel
import com.example.talkey_android.data.domain.model.users.RegisterToRequestModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class PostRegisterUseCase {
    suspend operator fun invoke(registerToRequestModel: RegisterToRequestModel): BaseResponse<RegisterFromResponseModel> {
        return DataProvider.postRegister(registerToRequestModel)
    }
}