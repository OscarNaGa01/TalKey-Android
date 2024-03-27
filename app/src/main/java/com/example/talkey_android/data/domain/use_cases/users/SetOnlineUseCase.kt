package com.example.talkey_android.data.domain.use_cases.users

import com.example.talkey_android.data.domain.model.common.CommonMessageModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class SetOnlineUseCase {
    suspend operator fun invoke(isOnline: Boolean): BaseResponse<CommonMessageModel> {
        return DataProvider.setOnline(isOnline)
    }
}