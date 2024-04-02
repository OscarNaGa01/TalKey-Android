package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.repository.remote.request.chats.ChatCreationRequest
import com.example.talkey_android.data.domain.repository.remote.request.messages.SendMessageRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.FirebaseTokenRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.LoginRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest
import com.example.talkey_android.data.domain.repository.remote.request.users.UpdateProfileRequest
import com.example.talkey_android.data.domain.repository.remote.response.chats.ChatCreationResponse
import com.example.talkey_android.data.domain.repository.remote.response.chats.ChatResponse
import com.example.talkey_android.data.domain.repository.remote.response.common.CommonMessageResponse
import com.example.talkey_android.data.domain.repository.remote.response.common.SuccessResponse
import com.example.talkey_android.data.domain.repository.remote.response.messages.ListMessageResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.LoginResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.RegisterResponse
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
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


    @POST("users/biometric")
    suspend fun loginBiometric(
        @Header("Authorization") token: String,
    ): Response<LoginResponse>

    @POST("users/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<CommonMessageResponse>

    @PUT("users/online/{isOnline}")
    suspend fun setOnline(
        @Header("Authorization") token: String,
        @Path("isOnline") isOnline: Boolean
    ): Response<CommonMessageResponse>

    @PUT("users/notification")
    suspend fun putNotification(
        @Header("Authorization") token: String,
        @Body firebaseTokenRequest: FirebaseTokenRequest
    ): Response<CommonMessageResponse>


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


    @POST("users/upload")
    suspend fun uploadImg(
        @Header("Authorization") token: String,
        @Body file: File
    ): Response<CommonMessageResponse>


    //CHATS--------------------------------------

    @GET("chats/view")
    suspend fun getListChats(
        @Header("Authorization") token: String
    ): Response<List<ChatResponse>>


    @POST("chats")
    suspend fun createChat(
        @Header("Authorization") token: String,
        @Body chatCreationRequest: ChatCreationRequest
    ): Response<ChatCreationResponse>

    @DELETE("chats/{idChat}")
    suspend fun deleteChat(
        @Header("Authorization") token: String,
        @Path("idChat") idChat: String
    ): Response<SuccessResponse>

    //MESSAGES--------------------------------------

    @POST("messages/new")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body sendMessageRequest: SendMessageRequest
    ): Response<SuccessResponse>

    @GET("messages/list/{idChat}")
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Path("idChat") idChat: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<ListMessageResponse>
}