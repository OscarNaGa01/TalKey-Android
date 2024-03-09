package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.RegisterToRequestModel
import com.example.talkey_android.data.domain.repository.remote.mapper.RequestMapper
import com.example.talkey_android.data.domain.repository.remote.request.users.RegisterRequest

class RegisterToRequestMappper : RequestMapper<RegisterToRequestModel, RegisterRequest> {
    override fun toRequest(model: RegisterToRequestModel): RegisterRequest {
        return RegisterRequest(
            model.login,
            model.password,
            model.nick,
            model.platform,
            model.firebaseToken
        )
    }
}