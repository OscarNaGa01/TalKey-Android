package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.repository.remote.request.users.LoginRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest
import com.example.talkey_android.data.domain.repository.remote.response.BaseApiCallService
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.LoginResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.LogoutResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.RegisterResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse

class ApiCallService(private val remoteApiService: RemoteApiService) : BaseApiCallService() {

    suspend fun postRegister(registerRequest: RegisterRequest): BaseResponse<RegisterResponse> {
        return apiCall { remoteApiService.postRegister(registerRequest) }
    }

    suspend fun postLogin(loginRequest: LoginRequest): BaseResponse<LoginResponse> {
        return apiCall { remoteApiService.postLogin(loginRequest) }
    }

    suspend fun postLogout(token: String): BaseResponse<LogoutResponse> {
        return apiCall { remoteApiService.postLogout(token) }
    }

    suspend fun getProfile(token: String): BaseResponse<UserFullDataResponse> {
        return apiCall { remoteApiService.getProfile(token) }
    }
}