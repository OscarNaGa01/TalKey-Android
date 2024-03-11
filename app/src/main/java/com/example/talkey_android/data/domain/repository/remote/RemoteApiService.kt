package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.repository.remote.request.users.FirebaseTokenRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.LoginRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.UpdateProfileRequest
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
import retrofit2.http.Path
import java.io.File

interface RemoteApiService {

    //USERS--------------------------------------

    //Register and login/logout------------------
    @POST("users/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    /**
     * This fun is not tested. Maybe doesn't work
     */
    @POST("users/biometric")
    suspend fun loginBiometric(
        @Body firebaseTokenRequest: FirebaseTokenRequest
    ): Response<LoginResponse>

    @POST("users/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<MessageResponse>

    @PUT("users/online/{isOnline}")
    suspend fun setOnline(
        @Header("Authorization") token: String,
        @Path("isOnline") isOnline: Boolean
    ): Response<MessageResponse>

    @PUT("users/notification")
    suspend fun putNotification(
        @Header("Authorization") token: String,
        @Body firebaseTokenRequest: FirebaseTokenRequest
    ): Response<MessageResponse>


    //Profile/s----------------------------------
    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserFullDataResponse>

    @GET("users")
    suspend fun getListProfiles(
        @Header("Authorization") token: String
    ): Response<List<UserFullDataResponse>>

    @PUT("users/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): Response<SuccessResponse>

    /**
     * This fun is not tested. Maybe doesn't work
     */
    @POST("users/upload")
    suspend fun uploadImg(
        @Header("Authorization") token: String,
        @Body file: File
    ): Response<MessageResponse>
}