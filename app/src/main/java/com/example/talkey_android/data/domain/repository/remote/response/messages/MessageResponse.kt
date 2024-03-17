package com.example.talkey_android.data.domain.repository.remote.response.messages

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("chat")
    val chat: String?,
    @SerializedName("source")
    val source: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("date")
    val date: String?
)
