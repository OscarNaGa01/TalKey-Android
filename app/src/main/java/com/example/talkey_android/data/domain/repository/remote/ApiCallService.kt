package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.repository.remote.request.users.FirebaseTokenRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.LoginRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.UpdateProfileRequest
import com.example.talkey_android.data.domain.repository.remote.response.BaseApiCallService
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.repository.remote.response.common.MessageResponse
import com.example.talkey_android.data.domain.repository.remote.response.common.SuccessResponse
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

    suspend fun logout(token: String): BaseResponse<MessageResponse> {
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

    suspend fun uploadImg(token: String, file: File): BaseResponse<MessageResponse> {
        return apiCall { remoteApiService.uploadImg(token, file) }
    }

    suspend fun setOnline(token: String, isOnline: Boolean): BaseResponse<MessageResponse> {
        return apiCall { remoteApiService.setOnline(token, isOnline) }
    }

    suspend fun putNotification(
        token: String,
        firebaseTokenRequest: FirebaseTokenRequest
    ): BaseResponse<MessageResponse> {
        return apiCall { remoteApiService.putNotification(token, firebaseTokenRequest) }
    }

    suspend fun loginBiometric(
        firebaseTokenRequest: FirebaseTokenRequest
    ): BaseResponse<LoginResponse> {
        return apiCall { remoteApiService.loginBiometric(firebaseTokenRequest) }
    }
}