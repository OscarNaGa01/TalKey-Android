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
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import java.io.File

interface DataSource {
    suspend fun postRegister(registerRequestModel: RegisterRequestModel): BaseResponse<RegisterResponseModel>
    suspend fun postLogin(loginRequestModel: LoginRequestModel): BaseResponse<UserModel>
    suspend fun postLogout(token: String): BaseResponse<MessageModel>
    suspend fun getProfile(token: String): BaseResponse<UserFullDataModel>
    suspend fun getListProfiles(token: String): BaseResponse<ListUsersModel>
    suspend fun putProfile(
        token: String,
        updateUserModel: UpdateUserModel
    ): BaseResponse<SuccessModel>

    suspend fun postUpload(token: String, file: File): BaseResponse<MessageModel>
    suspend fun putOnline(token: String, isOnline: Boolean): BaseResponse<MessageModel>
    suspend fun putNotification(token: String, firebaseToken: String): BaseResponse<MessageModel>
    suspend fun postBiometric(firebaseToken: String): BaseResponse<UserModel>

}