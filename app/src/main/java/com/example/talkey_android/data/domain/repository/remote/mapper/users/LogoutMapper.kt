package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.LogoutModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.users.LogoutResponse

class LogoutMapper : ResponseMapper<LogoutResponse, LogoutModel> {
    override fun fromResponse(response: LogoutResponse): LogoutModel {
        return LogoutModel(response.message ?: "")
    }
}