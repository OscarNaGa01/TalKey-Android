package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.repository.remote.request.chats.ChatCreationRequest
import com.example.talkey_android.data.domain.repository.remote.request.messages.SendMessageRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.FirebaseTokenRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.LoginRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.UpdateProfileRequest
import com.example.talkey_android.data.domain.repository.remote.response.BaseApiCallService
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.repository.remote.response.chats.ChatCreationResponse
import com.example.talkey_android.data.domain.repository.remote.response.chats.ChatResponse
import com.example.talkey_android.data.domain.repository.remote.response.common.CommonMessageResponse
import com.example.talkey_android.data.domain.repository.remote.response.common.SuccessResponse
import com.example.talkey_android.data.domain.repository.remote.response.messages.ListMessageResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.LoginResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.RegisterResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse
import java.io.File

class ApiCallService(private val remoteApiService: RemoteApiService) : BaseApiCallService() {

    suspend fun register(registerRequest: RegisterRequest): BaseResponse<RegisterResponse> {
        return apiCall { remoteApiService.register(registerRequest) }
    }

    suspend fun login(loginRequest: LoginRequest): BaseResponse<LoginResponse> {
        return apiCall { remoteApiService.login(loginRequest) }
    }

    suspend fun logout(token: String): BaseResponse<CommonMessageResponse> {
        return apiCall { remoteApiService.logout(token) }
    }

    suspend fun getProfile(token: String): BaseResponse<UserFullDataResponse> {
        return apiCall { remoteApiService.getProfile(token) }
    }

    suspend fun getListProfiles(token: String): BaseResponse<List<UserFullDataResponse>> {
        return apiCall { remoteApiService.getListProfiles(token) }
    }

    suspend fun updateProfile(
        token: String,
        updateProfileRequest: UpdateProfileRequest
    ): BaseResponse<SuccessResponse> {
        return apiCall { remoteApiService.updateProfile(token, updateProfileRequest) }
    }

    suspend fun uploadImg(token: String, file: File): BaseResponse<CommonMessageResponse> {
        return apiCall { remoteApiService.uploadImg(token, file) }
    }

    suspend fun setOnline(token: String, isOnline: Boolean): BaseResponse<CommonMessageResponse> {
        return apiCall { remoteApiService.setOnline(token, isOnline) }
    }

    suspend fun putNotification(
        token: String,
        firebaseTokenRequest: FirebaseTokenRequest
    ): BaseResponse<CommonMessageResponse> {
        return apiCall { remoteApiService.putNotification(token, firebaseTokenRequest) }
    }

    suspend fun loginBiometric(
        token: String
    ): BaseResponse<LoginResponse> {
        return apiCall { remoteApiService.loginBiometric(token) }
    }

    suspend fun getListChats(token: String): BaseResponse<List<ChatResponse>> {
        return apiCall { remoteApiService.getListChats(token) }
    }

    suspend fun createChat(
        token: String,
        chatCreationRequest: ChatCreationRequest
    ): BaseResponse<ChatCreationResponse> {
        return apiCall { remoteApiService.createChat(token, chatCreationRequest) }
    }

    suspend fun deleteChat(
        token: String,
        idChat: Int
    ): BaseResponse<SuccessResponse> {
        return apiCall { remoteApiService.deleteChat(token, idChat) }
    }

    suspend fun sendMessage(
        token: String,
        sendMessageRequest: SendMessageRequest
    ): BaseResponse<SuccessResponse> {
        return apiCall { remoteApiService.sendMessage(token, sendMessageRequest) }
    }

    suspend fun getMessages(
        token: String,
        idChat: String,
        limit: Int,
        offset: Int
    ): BaseResponse<ListMessageResponse> {
        return apiCall { remoteApiService.getMessages(token, idChat, limit, offset) }
    }
}