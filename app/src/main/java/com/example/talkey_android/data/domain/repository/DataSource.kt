package com.example.talkey_android.data.domain.repository

import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterResponseModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

interface DataSource {
    suspend fun postRegister(registerRequestModel: RegisterRequestModel): BaseResponse<RegisterResponseModel>
}