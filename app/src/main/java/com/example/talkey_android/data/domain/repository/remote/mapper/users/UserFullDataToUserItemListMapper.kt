package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.UserItemListModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse

class UserFullDataToUserItemListMapper : ResponseMapper<UserFullDataResponse, UserItemListModel> {
    override fun fromResponse(response: UserFullDataResponse): UserItemListModel {
        return UserItemListModel(
            response.id ?: "",
            response.nick ?: "",
            response.avatar ?: "",
            response.online ?: false,
            response.created ?: ""
        )
    }
}