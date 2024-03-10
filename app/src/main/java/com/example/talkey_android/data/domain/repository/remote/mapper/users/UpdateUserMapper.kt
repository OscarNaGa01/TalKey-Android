package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.UpdateUserModel
import com.example.talkey_android.data.domain.repository.remote.mapper.RequestMapper
import com.example.talkey_android.data.domain.repository.remote.request.users.UpdateUserRequest

class UpdateUserMapper : RequestMapper<UpdateUserModel, UpdateUserRequest> {
    override fun toRequest(model: UpdateUserModel): UpdateUserRequest {
        return UpdateUserRequest(
            model.password,
            model.nick
        )
    }
}