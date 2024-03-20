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

    override suspend fun logout(token: String): BaseResponse<CommonMessageModel> {
        return RemoteDataSource.logout(token)
    }

    override suspend fun getProfile(token: String): BaseResponse<UserProfileModel> {
        return RemoteDataSource.getProfile(token)
    }

    override suspend fun getListProfiles(token: String): BaseResponse<ListUsersModel> {
        return RemoteDataSource.getListProfiles(token)
    }

    override suspend fun updateProfile(
        token: String,
        updateProfileModel: UpdateProfileModel
    ): BaseResponse<SuccessModel> {
        return RemoteDataSource.updateProfile(token, updateProfileModel)
    }

    override suspend fun uploadImg(token: String, file: File): BaseResponse<CommonMessageModel> {
        return RemoteDataSource.uploadImg(token, file)
    }

    override suspend fun setOnline(token: String, isOnline: Boolean): BaseResponse<CommonMessageModel> {
        return RemoteDataSource.setOnline(token, isOnline)
    }

    override suspend fun putNotification(
        token: String,
        firebaseToken: String
    ): BaseResponse<CommonMessageModel> {
        return RemoteDataSource.putNotification(token, firebaseToken)
    }

    override suspend fun loginBiometric(token: String): BaseResponse<UserModel> {
        return RemoteDataSource.loginBiometric(token)
    }

    override suspend fun getListChats(token: String): BaseResponse<ListChatsModel> {
        return RemoteDataSource.getListChats(token)
    }

    override suspend fun createChat(
        token: String,
        source: String,
        target: String
    ): BaseResponse<ChatCreationModel> {
        return RemoteDataSource.createChat(token, source, target)
    }

    override suspend fun deleteChat(token: String, idChat: Int): BaseResponse<SuccessModel> {
        return RemoteDataSource.deleteChat(token, idChat)
    }

    override suspend fun sendMessage(
        token: String,
        chat: String,
        source: String,
        message: String
    ): BaseResponse<SuccessModel> {
        return RemoteDataSource.sendMessage(token, chat, source, message)
    }

    override suspend fun getMessages(
        token: String,
        idChat: String,
        limit: Int,
        offset: Int
    ): BaseResponse<ListMessageModel> {
        return RemoteDataSource.getMessages(token, idChat, limit, offset)
    }
}