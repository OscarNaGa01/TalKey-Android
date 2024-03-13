package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.ListUsersModel
import com.example.talkey_android.data.domain.model.users.UserItemListModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse

class ListUsersMapper : ResponseMapper<List<UserFullDataResponse>, ListUsersModel> {
    override fun fromResponse(response: List<UserFullDataResponse>): ListUsersModel {
        val resultModel = mutableListOf<UserItemListModel>()
        if (response.isNotEmpty()) {
            val userFullDataToUserItemListMapper = UserFullDataToUserItemListMapper()
            response.forEach {
                resultModel.add(userFullDataToUserItemListMapper.fromResponse(it))
            }
        }
        return ListUsersModel(
            resultModel
        )
    }
}