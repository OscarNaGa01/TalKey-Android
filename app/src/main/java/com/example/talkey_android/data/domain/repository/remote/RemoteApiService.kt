package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.repository.remote.request.users.LoginRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.UpdateUserRequest
import com.example.talkey_android.data.domain.repository.remote.response.common.MessageResponse
import com.example.talkey_android.data.domain.repository.remote.response.common.SuccessResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.LoginResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.RegisterResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import java.io.File

interface RemoteApiService {

    //USERS------------------------------------

    //Register and login/logout
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
    ): Response<MessageResponse>

    //Profile/s
    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserFullDataResponse>

    @GET("users")
    suspend fun getListProfiles(
        @Header("Authorization") token: String
    ): Response<List<UserFullDataResponse>>

    @PUT("users/profile")
    suspend fun putProfile(
        @Header("Authorization") token: String,
        @Body updateUserRequest: UpdateUserRequest
    ): Response<SuccessResponse>


    /**
     * This fun is not tested. Maybe is wrong
     */
    @POST("users/upload")
    suspend fun postUpload(
        @Header("Authorization") token: String,
        @Body file: File
    ): Response<MessageResponse>
}