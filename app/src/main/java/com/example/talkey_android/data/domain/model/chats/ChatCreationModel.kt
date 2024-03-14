package com.example.talkey_android.data.domain.model.chats

import com.example.talkey_android.data.domain.model.BaseModel

data class ChatCreationModel(
    val success: Boolean = false,
    val created: Boolean = false,
    val chatBasicInfoModel: ChatBasicInfoModel
) : BaseModel()