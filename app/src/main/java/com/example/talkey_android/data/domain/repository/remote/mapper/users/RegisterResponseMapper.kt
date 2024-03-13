package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.RegisterResponseModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.users.RegisterResponse

class RegisterResponseMapper : ResponseMapper<RegisterResponse, RegisterResponseModel> {
    override fun fromResponse(response: RegisterResponse): RegisterResponseModel {
        return RegisterResponseModel(response.success ?: false)
    }
}