package com.example.talkey_android.data.domain.repository

import com.example.talkey_android.data.domain.model.users.RegisterFromResponseModel
import com.example.talkey_android.data.domain.model.users.RegisterToRequestModel

interface DataSource {
    fun postRegister(registerToRequestModel: RegisterToRequestModel): BaseResponse<RegisterFromResponseModel>
}