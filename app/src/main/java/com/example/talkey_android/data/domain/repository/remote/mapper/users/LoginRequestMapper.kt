package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.repository.remote.mapper.RequestMapper
import com.example.talkey_android.data.domain.repository.remote.request.users.LoginRequest

class LoginRequestMapper : RequestMapper<LoginRequestModel, LoginRequest> {
    override fun toRequest(model: LoginRequestModel): LoginRequest {
        return LoginRequest(
            model.password,
            model.login,
            model.platform,
            model.firebaseToken
        )
    }
}