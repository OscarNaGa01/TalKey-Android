package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.UserFullDataModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse

class UserFullDataMapper : ResponseMapper<UserFullDataResponse, UserFullDataModel> {
    override fun fromResponse(response: UserFullDataResponse): UserFullDataModel {
        return UserFullDataModel(
            response.id ?: "",
            response.login ?: "",
            response.password ?: "",
            response.nick ?: "",
            response.platform ?: "",
            response.avatar ?: "",
            response.uuid ?: "",
            response.token ?: "",
            response.online ?: false,
            response.created ?: "",
            response.updated ?: ""
        )
    }
}