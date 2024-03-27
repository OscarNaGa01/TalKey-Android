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
import com.example.talkey_android.data.utils.Token
import java.io.File

class ApiCallService(private val remoteApiService: RemoteApiService) : BaseApiCallService() {

    suspend fun register(registerRequest: RegisterRequest): BaseResponse<RegisterResponse> {
        return apiCall { remoteApiService.register(registerRequest) }
    }

    suspend fun login(loginRequest: LoginRequest): BaseResponse<LoginResponse> {
        return apiCall { remoteApiService.login(loginRequest) }
    }

    suspend fun logout(): BaseResponse<CommonMessageResponse> {
        return apiCall { remoteApiService.logout(Token.getToken()) }
    }

    suspend fun getProfile(): BaseResponse<UserFullDataResponse> {
        return apiCall { remoteApiService.getProfile(Token.getToken()) }
    }

    suspend fun getListProfiles(): BaseResponse<List<UserFullDataResponse>> {
        return apiCall { remoteApiService.getListProfiles(Token.getToken()) }
    }

    suspend fun updateProfile(
        updateProfileRequest: UpdateProfileRequest
    ): BaseResponse<SuccessResponse> {
        return apiCall { remoteApiService.updateProfile(Token.getToken(), updateProfileRequest) }
    }

    suspend fun uploadImg(file: File): BaseResponse<CommonMessageResponse> {
        return apiCall { remoteApiService.uploadImg(Token.getToken(), file) }
    }

    suspend fun setOnline(isOnline: Boolean): BaseResponse<CommonMessageResponse> {
        return apiCall { remoteApiService.setOnline(Token.getToken(), isOnline) }
    }

    suspend fun putNotification(
        firebaseTokenRequest: FirebaseTokenRequest
    ): BaseResponse<CommonMessageResponse> {
        return apiCall { remoteApiService.putNotification(Token.getToken(), firebaseTokenRequest) }
    }

    suspend fun loginBiometric(
    ): BaseResponse<LoginResponse> {
        return apiCall { remoteApiService.loginBiometric(Token.getToken()) }
    }

    suspend fun getListChats(): BaseResponse<List<ChatResponse>> {
        return apiCall { remoteApiService.getListChats(Token.getToken()) }
    }

    suspend fun createChat(
        chatCreationRequest: ChatCreationRequest
    ): BaseResponse<ChatCreationResponse> {
        return apiCall { remoteApiService.createChat(Token.getToken(), chatCreationRequest) }
    }

    suspend fun deleteChat(
        idChat: String
    ): BaseResponse<SuccessResponse> {
        return apiCall { remoteApiService.deleteChat(Token.getToken(), idChat) }
    }

    suspend fun sendMessage(
        sendMessageRequest: SendMessageRequest
    ): BaseResponse<SuccessResponse> {
        return apiCall { remoteApiService.sendMessage(Token.getToken(), sendMessageRequest) }
    }

    suspend fun getMessages(
        idChat: String,
        limit: Int,
        offset: Int
    ): BaseResponse<ListMessageResponse> {
        return apiCall { remoteApiService.getMessages(Token.getToken(), idChat, limit, offset) }
    }
}