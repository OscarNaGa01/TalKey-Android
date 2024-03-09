package com.example.talkey_android.data.domain.repository

import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterResponseModel
import com.example.talkey_android.data.domain.repository.remote.RemoteDataSource
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

object DataProvider : DataSource {
    override suspend fun postRegister(
        registerRequestModel: RegisterRequestModel
    ): BaseResponse<RegisterResponseModel> {
        return RemoteDataSource.postRegister(registerRequestModel)
    }
}