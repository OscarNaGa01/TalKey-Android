package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest
import com.example.talkey_android.data.domain.repository.remote.response.BaseApiCallService
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.RegisterResponse

class ApiCallService(private val remoteApiService: RemoteApiService) : BaseApiCallService() {

    suspend fun postRegister(registerRequest: RegisterRequest): BaseResponse<RegisterResponse> {
        return apiCall { remoteApiService.postRegister(registerRequest) }
    }
}