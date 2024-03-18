package com.example.talkey_android.data.domain.repository.remote.mapper.common

import com.example.talkey_android.data.domain.model.common.CommonMessageModel
import com.example.talkey_android.data.domain.repository.remote.mapper.ResponseMapper
import com.example.talkey_android.data.domain.repository.remote.response.common.CommonMessageResponse

class CommonMessageMapper : ResponseMapper<CommonMessageResponse, CommonMessageModel> {
    override fun fromResponse(response: CommonMessageResponse): CommonMessageModel {
        return CommonMessageModel(response.message ?: "")
    }
}