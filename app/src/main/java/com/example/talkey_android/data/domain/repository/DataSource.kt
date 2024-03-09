package com.example.talkey_android.data.domain.repository

import com.example.talkey_android.data.domain.model.users.RegisterFromResponseModel
import com.example.talkey_android.data.domain.model.users.RegisterToRequestModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

interface DataSource {
    suspend fun postRegister(registerToRequestModel: RegisterToRequestModel): BaseResponse<RegisterFromResponseModel>
}