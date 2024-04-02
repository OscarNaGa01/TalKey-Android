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
import com.example.talkey_android.data.domain.repository.remote.mapper.users.RegisterRequestMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.RegisterResponseMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.UpdateProfileMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.UserFullDataToUserProfileMapper
import com.example.talkey_android.data.domain.repository.remote.request.chats.ChatCreationRequest
import com.example.talkey_android.data.domain.repository.remote.request.messages.SendMessageRequest
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import java.io.File

object RemoteDataSource : DataSource {

    private val apiCallService = ApiCallService(RetrofitClient.getApiServices())

    override suspend fun register(registerRequestModel: RegisterRequestModel)
            : BaseResponse<RegisterResponseModel> {
        val apiResult =
            apiCallService.register(RegisterRequestMapper().toRequest(registerRequestModel))
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

    override suspend fun loginBiometric(): BaseResponse<UserModel> {
        val apiResult =
            apiCallService.loginBiometric()

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(LoginResponseToUserModelMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun logout(): BaseResponse<CommonMessageModel> {
        return when (val apiResult = apiCallService.logout()) {
            is BaseResponse.Success ->
                BaseResponse.Success(CommonMessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun getProfile(): BaseResponse<UserProfileModel> {
        return when (val apiResult = apiCallService.getProfile()) {
            is BaseResponse.Success ->
                BaseResponse.Success(UserFullDataToUserProfileMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun getListProfiles(): BaseResponse<ListUsersModel> {
        return when (val apiResult = apiCallService.getListProfiles()) {
            is BaseResponse.Success ->
                BaseResponse.Success(ListUsersMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun updateProfile(
        updateProfileModel: UpdateProfileModel
    ): BaseResponse<SuccessModel> {
        val apiResult =
            apiCallService.updateProfile(UpdateProfileMapper().toRequest(updateProfileModel))

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(SuccessMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun uploadImg(file: File): BaseResponse<CommonMessageModel> {
        return when (val apiResult = apiCallService.uploadImg(file)) {
            is BaseResponse.Success ->
                BaseResponse.Success(CommonMessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun setOnline(isOnline: Boolean): BaseResponse<CommonMessageModel> {

        return when (val apiResult = apiCallService.setOnline(isOnline)) {
            is BaseResponse.Success ->
                BaseResponse.Success(CommonMessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun putNotification(
        firebaseToken: String
    ): BaseResponse<CommonMessageModel> {
        val apiResult =
            apiCallService.putNotification(FirebaseTokenMapper().toRequest(firebaseToken))

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(CommonMessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun getListChats(): BaseResponse<ListChatsModel> {
        return when (val apiResult = apiCallService.getListChats()) {
            is BaseResponse.Success ->
                BaseResponse.Success(ListChatsMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun createChat(
        source: String,
        target: String
    ): BaseResponse<ChatCreationModel> {
        val apiResult = apiCallService.createChat(
            ChatCreationRequest(source, target)
        )
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(ChatCreationMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun deleteChat(idChat: String): BaseResponse<SuccessModel> {

        return when (val apiResult = apiCallService.deleteChat(idChat)) {
            is BaseResponse.Success ->
                BaseResponse.Success(SuccessMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun sendMessage(
        chat: String,
        source: String,
        message: String
    ): BaseResponse<SuccessModel> {
        val apiResult = apiCallService.sendMessage(
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
        idChat: String,
        limit: Int,
        offset: Int
    ): BaseResponse<ListMessageModel> {

        return when (val apiResult = apiCallService.getMessages(idChat, limit, offset)) {
            is BaseResponse.Success ->
                BaseResponse.Success(ListMessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }
}