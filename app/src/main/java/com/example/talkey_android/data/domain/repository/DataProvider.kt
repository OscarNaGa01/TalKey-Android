package com.example.talkey_android.data.domain.repository

import com.example.talkey_android.data.domain.model.users.RegisterFromResponseModel
import com.example.talkey_android.data.domain.model.users.RegisterToRequestModel
import com.example.talkey_android.data.domain.repository.remote.RemoteDataSource
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

object DataProvider : DataSource {
    override suspend fun postRegister(
        registerToRequestModel: RegisterToRequestModel
    ): BaseResponse<RegisterFromResponseModel> {
        return RemoteDataSource.postRegister(registerToRequestModel)
    }
}