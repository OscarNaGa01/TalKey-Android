package com.example.talkey_android.data.domain.repository.remote.request.messages

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(
    @SerializedName("chat")
    val chat: String?,
    @SerializedName("source")
    val source: String?,
    @SerializedName("message")
    val message: String?
)
