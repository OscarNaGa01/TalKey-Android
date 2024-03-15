package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.model.chats.ChatCreationModel
import com.example.talkey_android.data.domain.model.chats.ListChatsModel
import com.example.talkey_android.data.domain.model.common.CommonMessageModel
import com.example.talkey_android.data.domain.model.common.SuccessModel
import com.example.talkey_android.data.domain.model.messages.ListMessageModel
import com.example.talkey_android.data.domain.model.users.ListUsersModel
import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterResponseModel
import com.example.talkey_android.data.domain.model.users.UpdateProfileModel
import com.example.talkey_android.data.domain.model.users.UserModel
import com.example.talkey_android.data.domain.model.users.UserProfileModel
import com.example.talkey_android.data.domain.repository.DataSource
import com.example.talkey_android.data.domain.repository.remote.mapper.chats.ChatCreationMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.chats.ListChatsMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.common.CommonMessageMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.common.SuccessMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.messages.ListMessageMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.FirebaseTokenMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.ListUsersMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.LoginRequestMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.LoginResponseToUserModelMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.RegisterRequestMappper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.RegisterResponseMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.UpdateProfileMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.UserFullDataToUserProfileMapper
import com.example.talkey_android.data.domain.repository.remote.request.chats.ChatCreationRequest
import com.example.talkey_android.data.domain.repository.remote.request.messages.SendMessageRequest
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import java.io.File

object RemoteDataSource : DataSource {

    //Aquí está la variable que iría en la inyección de dependencias
    private val apiCallService = ApiCallService(RetrofitClient.getApiServices())

    override suspend fun register(registerRequestModel: RegisterRequestModel)
            : BaseResponse<RegisterResponseModel> {
        val apiResult =
            apiCallService.register(RegisterRequestMappper().toRequest(registerRequestModel))
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(RegisterResponseMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun login(loginRequestModel: LoginRequestModel): BaseResponse<UserModel> {
        val apiResult =
            apiCallService.login(LoginRequestMapper().toRequest(loginRequestModel))
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(LoginResponseToUserModelMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun loginBiometric(
        firebaseToken: String
    ): BaseResponse<UserModel> {
        val apiResult =
            apiCallService.loginBiometric(FirebaseTokenMapper().toRequest(firebaseToken))

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(LoginResponseToUserModelMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun logout(token: String): BaseResponse<CommonMessageModel> {
        val apiResult = apiCallService.logout(token)
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(CommonMessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun getProfile(token: String): BaseResponse<UserProfileModel> {
        val apiResult = apiCallService.getProfile(token)
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(UserFullDataToUserProfileMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun getListProfiles(token: String): BaseResponse<ListUsersModel> {
        val apiResult = apiCallService.getListProfiles(token)
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(ListUsersMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun updateProfile(
        token: String,
        updateProfileModel: UpdateProfileModel
    ): BaseResponse<SuccessModel> {
        val apiResult =
            apiCallService.updateProfile(token, UpdateProfileMapper().toRequest(updateProfileModel))

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(SuccessMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun uploadImg(token: String, file: File): BaseResponse<CommonMessageModel> {
        val apiResult = apiCallService.uploadImg(token, file)
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(CommonMessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun setOnline(token: String, isOnline: Boolean): BaseResponse<CommonMessageModel> {
        val apiResult = apiCallService.setOnline(token, isOnline)

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(CommonMessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun putNotification(
        token: String,
        firebaseToken: String
    ): BaseResponse<CommonMessageModel> {
        val apiResult =
            apiCallService.putNotification(token, FirebaseTokenMapper().toRequest(firebaseToken))

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(CommonMessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun getListChats(token: String): BaseResponse<ListChatsModel> {
        val apiResult = apiCallService.getListChats(token)
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(ListChatsMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun createChat(
        token: String,
        source: String,
        target: String
    ): BaseResponse<ChatCreationModel> {
        val apiResult = apiCallService.createChat(
            token,
            ChatCreationRequest(source, target)
        )
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(ChatCreationMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun deleteChat(token: String, idChat: Int): BaseResponse<SuccessModel> {
        val apiResult = apiCallService.deleteChat(token, idChat)

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(SuccessMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun sendMessage(
        token: String,
        chat: String,
        source: String,
        message: String
    ): BaseResponse<SuccessModel> {
        val apiResult = apiCallService.sendMessage(
            token,
            SendMessageRequest(chat, source, message)
        )
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(SuccessMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun getMessages(
        token: String,
        idChat: Int,
        limit: Int,
        offset: Int
    ): BaseResponse<ListMessageModel> {
        val apiResult = apiCallService.getMessages(token, idChat, limit, offset)

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(ListMessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }
}