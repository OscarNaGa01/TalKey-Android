package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.UserProfileModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse

class UserFullDataToUserProfileMapper : ResponseMapper<UserFullDataResponse, UserProfileModel> {
    override fun fromResponse(response: UserFullDataResponse): UserProfileModel {
        return UserProfileModel(
            response.id ?: "",
            response.login ?: "",
            response.nick ?: "",
            response.avatar ?: "",
            response.token ?: "",
            response.online ?: false,
            response.password ?: ""
        )
    }
}