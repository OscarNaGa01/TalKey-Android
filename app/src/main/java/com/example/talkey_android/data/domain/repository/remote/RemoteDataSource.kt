package com.example.talkey_android.data.domain.repository.remote

import com.example.talkey_android.data.domain.model.common.MessageModel
import com.example.talkey_android.data.domain.model.common.SuccessModel
import com.example.talkey_android.data.domain.model.users.ListUsersModel
import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterResponseModel
import com.example.talkey_android.data.domain.model.users.UpdateUserModel
import com.example.talkey_android.data.domain.model.users.UserFullDataModel
import com.example.talkey_android.data.domain.model.users.UserModel
import com.example.talkey_android.data.domain.repository.DataSource
import com.example.talkey_android.data.domain.repository.remote.mapper.common.MessageMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.common.SuccessMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.ListUsersMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.LoginRequestMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.LoginResponseToUserModelMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.RegisterRequestMappper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.RegisterResponseMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.UpdateUserMapper
import com.example.talkey_android.data.domain.repository.remote.mapper.users.UserFullDataMapper
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import java.io.File

object RemoteDataSource : DataSource {

    //Aquí está la variable que iría en la inyección de dependencias
    private val apiCallService = ApiCallService(RetrofitClient.getApiServices())

    override suspend fun postRegister(registerRequestModel: RegisterRequestModel)
            : BaseResponse<RegisterResponseModel> {
        val apiResult =
            apiCallService.postRegister(RegisterRequestMappper().toRequest(registerRequestModel))
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(RegisterResponseMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun postLogin(loginRequestModel: LoginRequestModel): BaseResponse<UserModel> {
        val apiResult =
            apiCallService.postLogin(LoginRequestMapper().toRequest(loginRequestModel))
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(LoginResponseToUserModelMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun postLogout(token: String): BaseResponse<MessageModel> {
        val apiResult = apiCallService.postLogout(token)
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(MessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun getProfile(token: String): BaseResponse<UserFullDataModel> {
        val apiResult = apiCallService.getProfile(token)
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(UserFullDataMapper().fromResponse(apiResult.data))

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

    override suspend fun putProfile(
        token: String,
        updateUserModel: UpdateUserModel
    ): BaseResponse<SuccessModel> {
        val apiResult =
            apiCallService.putProfile(token, UpdateUserMapper().toRequest(updateUserModel))

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(SuccessMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun postUpload(token: String, file: File): BaseResponse<MessageModel> {
        val apiResult = apiCallService.postUpload(token, file)
        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(MessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }

    override suspend fun putOnline(token: String, isOnline: Boolean): BaseResponse<MessageModel> {
        val apiResult = apiCallService.putOnline(token, isOnline)

        return when (apiResult) {
            is BaseResponse.Success ->
                BaseResponse.Success(MessageMapper().fromResponse(apiResult.data))

            is BaseResponse.Error ->
                BaseResponse.Error(apiResult.error)
        }
    }
}