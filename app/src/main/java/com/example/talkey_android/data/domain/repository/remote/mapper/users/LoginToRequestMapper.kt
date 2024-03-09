package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.LoginToRequestModel
import com.example.talkey_android.data.domain.repository.remote.mapper.RequestMapper
import com.example.talkey_android.data.domain.repository.remote.request.users.LoginRequest

class LoginToRequestMapper : RequestMapper<LoginToRequestModel, LoginRequest> {
    override fun toRequest(model: LoginToRequestModel): LoginRequest {
        return LoginRequest(
            model.password,
            model.login,
            model.platform,
            model.firebaseToken
        )
    }
}