package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.repository.remote.request.users.LoginRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest
import com.example.talkey_android.data.domain.repository.remote.response.users.LoginResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.LogoutResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.RegisterResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RemoteApiService {

    //USERS------------------------------------

    //Registro y login/logout
    @POST("users/register")
    suspend fun postRegister(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("users/login")
    suspend fun postLogin(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("users/logout")
    suspend fun postLogout(
        @Header("Authorization") token: String
    ): Response<LogoutResponse>

    //Perfil/es
    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserFullDataResponse>
}