package com.example.talkey_android.data.domain.repository

import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.LogoutModel
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterResponseModel
import com.example.talkey_android.data.domain.model.users.UserModel
import com.example.talkey_android.data.domain.repository.remote.RemoteDataSource
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

object DataProvider : DataSource {
    override suspend fun postRegister(
        registerRequestModel: RegisterRequestModel
    ): BaseResponse<RegisterResponseModel> {
        return RemoteDataSource.postRegister(registerRequestModel)
    }

    override suspend fun postLogin(loginRequestModel: LoginRequestModel): BaseResponse<UserModel> {
        return RemoteDataSource.postLogin(loginRequestModel)
    }

    override suspend fun postLogout(token: String): BaseResponse<LogoutModel> {
        return RemoteDataSource.postLogout(token)
    }
}