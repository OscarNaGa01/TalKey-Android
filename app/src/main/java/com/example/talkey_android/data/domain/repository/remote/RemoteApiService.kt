package com.example.talkey_android.data.domain.repository.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RemoteApiService {

    @POST("users/register")
    suspend fun postRegister(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>
    
}