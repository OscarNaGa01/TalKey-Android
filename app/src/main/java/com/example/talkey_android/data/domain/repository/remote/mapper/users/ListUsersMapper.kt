package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.ListUsersModel
import com.example.talkey_android.data.domain.model.users.UserFullDataModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.users.UserFullDataResponse

class ListUsersMapper : ResponseMapper<List<UserFullDataResponse>, ListUsersModel> {
    override fun fromResponse(response: List<UserFullDataResponse>): ListUsersModel {
        val resultModel = mutableListOf<UserFullDataModel>()
        if (response.isNotEmpty()) {
            val userFullDataMapper = UserFullDataMapper()
            response.forEach {
                resultModel.add(userFullDataMapper.fromResponse(it))
            }
        }
        return ListUsersModel(
            resultModel
        )
    }

    /*override fun fromResponse(response: ListUsersResponse): ListUsersModel {
        val resultModel = mutableListOf<UserFullDataModel>()

        if (!response.users.isNullOrEmpty()) {
            val userFullDataMapper = UserFullDataMapper()
            response.users.forEach {
                resultModel.add(userFullDataMapper.fromResponse(it))
            }
        }
        return ListUsersModel(
            resultModel
        )
    }*/
}