package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.model.users.UpdateProfileModel
import com.example.talkey_android.data.domain.repository.remote.mapper.RequestMapper
import com.example.talkey_android.data.domain.repository.remote.request.users.UpdateProfileRequest

class UpdateProfileMapper : RequestMapper<UpdateProfileModel, UpdateProfileRequest> {
    override fun toRequest(model: UpdateProfileModel): UpdateProfileRequest {
        return UpdateProfileRequest(
            model.password,
            model.nick
        )
    }
}