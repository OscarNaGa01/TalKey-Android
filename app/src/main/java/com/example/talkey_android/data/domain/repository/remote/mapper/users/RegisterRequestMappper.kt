package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.repository.remote.mapper.RequestMapper
import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest

class RegisterRequestMappper : RequestMapper<RegisterRequestModel, RegisterRequest> {
    override fun toRequest(model: RegisterRequestModel): RegisterRequest {
        return RegisterRequest(
            model.login,
            model.password,
            model.nick,
            model.platform,
            model.firebaseToken
        )
    }
}