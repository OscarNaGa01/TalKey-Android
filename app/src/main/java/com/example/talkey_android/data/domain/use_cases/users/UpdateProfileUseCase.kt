package com.example.talkey_android.data.domain.use_cases.users

import com.example.talkey_android.data.domain.model.common.SuccessModel
import com.example.talkey_android.data.domain.model.users.UpdateUserModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class UpdateProfileUseCase {
    suspend operator fun invoke(
        token: String,
        updateUserModel: UpdateUserModel
    ): BaseResponse<SuccessModel> {
        return DataProvider.updateProfile(token, updateUserModel)
    }
}