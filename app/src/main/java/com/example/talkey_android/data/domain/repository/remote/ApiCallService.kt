package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.repository.remote.request.users.LoginRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.UpdateUserRequest
import com.example.talkey_android.data.domain.repository.remote.response.BaseApiCallService
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.repository.remote.response.common.MessageResponse
import com.example.talkey_android.data.domain.repository.remote.response.common.SuccessResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.LoginResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.RegisterResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse
import java.io.File

class ApiCallService(private val remoteApiService: RemoteApiService) : BaseApiCallService() {

    suspend fun postRegister(registerRequest: RegisterRequest): BaseResponse<RegisterResponse> {
        return apiCall { remoteApiService.postRegister(registerRequest) }
    }

    suspend fun postLogin(loginRequest: LoginRequest): BaseResponse<LoginResponse> {
        return apiCall { remoteApiService.postLogin(loginRequest) }
    }

    suspend fun postLogout(token: String): BaseResponse<MessageResponse> {
        return apiCall { remoteApiService.postLogout(token) }
    }

    suspend fun getProfile(token: String): BaseResponse<UserFullDataResponse> {
        return apiCall { remoteApiService.getProfile(token) }
    }

    suspend fun getListProfiles(token: String): BaseResponse<List<UserFullDataResponse>> {
        return apiCall { remoteApiService.getListProfiles(token) }
    }

    suspend fun putProfile(
        token: String,
        updateUserRequest: UpdateUserRequest
    ): BaseResponse<SuccessResponse> {
        return apiCall { remoteApiService.putProfile(token, updateUserRequest) }
    }

    suspend fun postUpload(token: String, file: File): BaseResponse<MessageResponse> {
        return apiCall { remoteApiService.postUpload(token, file) }
    }

    suspend fun putOnline(token: String, isOnline: Boolean): BaseResponse<MessageResponse> {
        return apiCall { remoteApiService.putOnline(token, isOnline) }
    }

}