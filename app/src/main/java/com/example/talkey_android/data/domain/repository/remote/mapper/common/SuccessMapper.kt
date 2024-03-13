package com.example.talkey_android.data.domain.repository.remote.mapper.common

import com.example.talkey_android.data.domain.model.common.SuccessModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.common.SuccessResponse

class SuccessMapper : ResponseMapper<SuccessResponse, SuccessModel> {
    override fun fromResponse(response: SuccessResponse): SuccessModel {
        return SuccessModel(response.success ?: false)
    }
}