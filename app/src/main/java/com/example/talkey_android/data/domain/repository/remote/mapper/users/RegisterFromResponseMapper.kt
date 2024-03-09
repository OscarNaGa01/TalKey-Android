package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.RegisterFromResponseModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.users.RegisterResponse

class RegisterFromResponseMapper : ResponseMapper<RegisterResponse, RegisterFromResponseModel> {
    override fun fromResponse(response: RegisterResponse): RegisterFromResponseModel {
        return RegisterFromResponseModel(response.success ?: false)
    }
}