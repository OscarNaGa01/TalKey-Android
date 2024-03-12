package com.example.talkey_android.data.domain.repository.remote.response.chats

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("source")
    val source: String?,
    @SerializedName("target")
    val target: String?,
    @SerializedName("created")
    val created: String?,
)
