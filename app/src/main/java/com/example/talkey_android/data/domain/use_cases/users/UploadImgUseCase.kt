package com.example.talkey_android.data.domain.use_cases.users

import com.example.talkey_android.data.domain.model.common.MessageModel
import com.example.talkey_android.data.domain.repository.DataProvider
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import java.io.File

class UploadImgUseCase {
    suspend operator fun invoke(token: String, file: File): BaseResponse<MessageModel> {
        return DataProvider.uploadImg(token, file)
    }
}