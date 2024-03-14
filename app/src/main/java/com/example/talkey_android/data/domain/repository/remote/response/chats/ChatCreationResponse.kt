package com.example.talkey_android.data.domain.repository.remote.response.chats

import com.google.gson.annotations.SerializedName

data class ChatCreationResponse(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("created")
    val created: Boolean?,
    @SerializedName("chat")
    val chat: ChatBasicInfoResponse?,

    )