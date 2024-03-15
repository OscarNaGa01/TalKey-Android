package com.example.talkey_android.data.domain.repository

import com.example.talkey_android.data.domain.model.chats.ChatCreationModel
import com.example.talkey_android.data.domain.model.chats.ListChatsModel
import com.example.talkey_android.data.domain.model.common.CommonMessageModel
import com.example.talkey_android.data.domain.model.common.SuccessModel
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
    suspend fun logout(token: String): BaseResponse<CommonMessageModel>
    suspend fun getProfile(token: String): BaseResponse<UserProfileModel>
    suspend fun getListProfiles(token: String): BaseResponse<ListUsersModel>
    suspend fun updateProfile(
        token: String,
        updateProfileModel: UpdateProfileModel
    ): BaseResponse<SuccessModel>

    suspend fun uploadImg(token: String, file: File): BaseResponse<CommonMessageModel>
    suspend fun setOnline(token: String, isOnline: Boolean): BaseResponse<CommonMessageModel>
    suspend fun putNotification(token: String, firebaseToken: String): BaseResponse<CommonMessageModel>
    suspend fun loginBiometric(firebaseToken: String): BaseResponse<UserModel>
    suspend fun getListChats(token: String): BaseResponse<ListChatsModel>
    suspend fun createChat(
        token: String,
        source: String,
        target: String
    ): BaseResponse<ChatCreationModel>

    suspend fun deleteChat(token: String, idChat: Int): BaseResponse<SuccessModel>
}