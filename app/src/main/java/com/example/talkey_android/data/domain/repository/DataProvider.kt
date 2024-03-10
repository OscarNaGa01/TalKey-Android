package com.example.talkey_android.data.domain.repository

import com.example.talkey_android.data.domain.model.common.MessageModel
import com.example.talkey_android.data.domain.model.common.SuccessModel
import com.example.talkey_android.data.domain.model.users.ListUsersModel
import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterResponseModel
import com.example.talkey_android.data.domain.model.users.UpdateUserModel
import com.example.talkey_android.data.domain.model.users.UserFullDataModel
import com.example.talkey_android.data.domain.model.users.UserModel
import com.example.talkey_android.data.domain.repository.remote.RemoteDataSource
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import java.io.File

object DataProvider : DataSource {
    override suspend fun postRegister(
        registerRequestModel: RegisterRequestModel
    ): BaseResponse<RegisterResponseModel> {
        return RemoteDataSource.postRegister(registerRequestModel)
    }

    override suspend fun postLogin(loginRequestModel: LoginRequestModel): BaseResponse<UserModel> {
        return RemoteDataSource.postLogin(loginRequestModel)
    }

    override suspend fun postLogout(token: String): BaseResponse<MessageModel> {
        return RemoteDataSource.postLogout(token)
    }

    override suspend fun getProfile(token: String): BaseResponse<UserFullDataModel> {
        return RemoteDataSource.getProfile(token)
    }

    override suspend fun getListProfiles(token: String): BaseResponse<ListUsersModel> {
        return RemoteDataSource.getListProfiles(token)
    }

    override suspend fun putProfile(
        token: String,
        updateUserModel: UpdateUserModel
    ): BaseResponse<SuccessModel> {
        return RemoteDataSource.putProfile(token, updateUserModel)
    }

    override suspend fun postUpload(token: String, file: File): BaseResponse<MessageModel> {
        return RemoteDataSource.postUpload(token, file)
    }

    override suspend fun putOnline(token: String, isOnline: Boolean): BaseResponse<MessageModel> {
        return RemoteDataSource.putOnline(token, isOnline)
    }

    override suspend fun putNotification(
        token: String,
        firebaseToken: String
    ): BaseResponse<MessageModel> {
        return RemoteDataSource.putNotification(token, firebaseToken)
    }

    override suspend fun postBiometric(firebaseToken: String): BaseResponse<UserModel> {
        return RemoteDataSource.postBiometric(firebaseToken)
    }
}