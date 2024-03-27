package com.example.talkey_android.data.domain.repository

import com.example.talkey_android.data.domain.model.chats.ChatCreationModel
import com.example.talkey_android.data.domain.model.chats.ListChatsModel
import com.example.talkey_android.data.domain.model.common.CommonMessageModel
import com.example.talkey_android.data.domain.model.common.SuccessModel
import com.example.talkey_android.data.domain.model.messages.ListMessageModel
import com.example.talkey_android.data.domain.model.users.ListUsersModel
import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterResponseModel
import com.example.talkey_android.data.domain.model.users.UpdateProfileModel
import com.example.talkey_android.data.domain.model.users.UserModel
import com.example.talkey_android.data.domain.model.users.UserProfileModel
import com.example.talkey_android.data.domain.repository.remote.RemoteDataSource
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import java.io.File

object DataProvider : DataSource {
    override suspend fun register(
        registerRequestModel: RegisterRequestModel
    ): BaseResponse<RegisterResponseModel> {
        return RemoteDataSource.register(registerRequestModel)
    }

    override suspend fun login(loginRequestModel: LoginRequestModel): BaseResponse<UserModel> {
        return RemoteDataSource.login(loginRequestModel)
    }

    override suspend fun logout(): BaseResponse<CommonMessageModel> {
        return RemoteDataSource.logout()
    }

    override suspend fun getProfile(): BaseResponse<UserProfileModel> {
        return RemoteDataSource.getProfile()
    }

    override suspend fun getListProfiles(): BaseResponse<ListUsersModel> {
        return RemoteDataSource.getListProfiles()
    }

    override suspend fun updateProfile(
        updateProfileModel: UpdateProfileModel
    ): BaseResponse<SuccessModel> {
        return RemoteDataSource.updateProfile(updateProfileModel)
    }

    override suspend fun uploadImg(file: File): BaseResponse<CommonMessageModel> {
        return RemoteDataSource.uploadImg(file)
    }

    override suspend fun setOnline(isOnline: Boolean): BaseResponse<CommonMessageModel> {
        return RemoteDataSource.setOnline(isOnline)
    }

    override suspend fun putNotification(
        firebaseToken: String
    ): BaseResponse<CommonMessageModel> {
        return RemoteDataSource.putNotification(firebaseToken)
    }

    override suspend fun loginBiometric(): BaseResponse<UserModel> {
        return RemoteDataSource.loginBiometric()
    }

    override suspend fun getListChats(): BaseResponse<ListChatsModel> {
        return RemoteDataSource.getListChats()
    }

    override suspend fun createChat(
        source: String,
        target: String
    ): BaseResponse<ChatCreationModel> {
        return RemoteDataSource.createChat(source, target)
    }

    override suspend fun deleteChat(idChat: String): BaseResponse<SuccessModel> {
        return RemoteDataSource.deleteChat(idChat)
    }

    override suspend fun sendMessage(
        chat: String,
        source: String,
        message: String
    ): BaseResponse<SuccessModel> {
        return RemoteDataSource.sendMessage(chat, source, message)
    }

    override suspend fun getMessages(
        idChat: String,
        limit: Int,
        offset: Int
    ): BaseResponse<ListMessageModel> {
        return RemoteDataSource.getMessages(idChat, limit, offset)
    }
}