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
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import java.io.File

interface DataSource {
    suspend fun register(registerRequestModel: RegisterRequestModel): BaseResponse<RegisterResponseModel>
    suspend fun login(loginRequestModel: LoginRequestModel): BaseResponse<UserModel>
    suspend fun logout(): BaseResponse<CommonMessageModel>
    suspend fun getProfile(): BaseResponse<UserProfileModel>
    suspend fun getListProfiles(): BaseResponse<ListUsersModel>
    suspend fun updateProfile(
        updateProfileModel: UpdateProfileModel
    ): BaseResponse<SuccessModel>

    suspend fun uploadImg(file: File): BaseResponse<CommonMessageModel>
    suspend fun setOnline(isOnline: Boolean): BaseResponse<CommonMessageModel>
    suspend fun putNotification(
        firebaseToken: String
    ): BaseResponse<CommonMessageModel>

    suspend fun loginBiometric(): BaseResponse<UserModel>
    suspend fun getListChats(): BaseResponse<ListChatsModel>
    suspend fun createChat(
        source: String,
        target: String
    ): BaseResponse<ChatCreationModel>

    suspend fun deleteChat(idChat: String): BaseResponse<SuccessModel>
    suspend fun sendMessage(
        chat: String,
        source: String,
        message: String
    ): BaseResponse<SuccessModel>

    suspend fun getMessages(
        idChat: String,
        limit: Int,
        offset: Int
    ): BaseResponse<ListMessageModel>
}