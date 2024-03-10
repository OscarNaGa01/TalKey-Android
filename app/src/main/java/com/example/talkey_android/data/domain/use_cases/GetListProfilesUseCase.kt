package com.example.talkey_android.data.domain.use_cases

import com.example.talkey_android.data.domain.model.users.ListUsersModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse

class GetListProfilesUseCase {

    suspend operator fun invoke(token: String): BaseResponse<ListUsersModel> {
        return DataProvider.getListProfiles(token)
    }
}