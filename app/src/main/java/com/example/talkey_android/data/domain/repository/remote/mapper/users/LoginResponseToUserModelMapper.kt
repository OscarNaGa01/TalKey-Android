package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.UserModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.users.LoginResponse

class LoginResponseToUserModelMapper : ResponseMapper<LoginResponse, UserModel> {
    override fun fromResponse(response: LoginResponse): UserModel {
        
        return UserModel(
            response.user?.id ?: "",
            response.user?.nick ?: "",
            response.user?.avatar ?: "",
            response.user?.online ?: false,
            response.token ?: ""
        )
    }
}