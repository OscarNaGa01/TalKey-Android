package com.example.talkey_android.data.domain.repository.remote.request.chats

import com.google.gson.annotations.SerializedName

data class ChatCreationRequest(
    @SerializedName("source")
    val source: String?,
    @SerializedName("target")
    val target: String?,
)
