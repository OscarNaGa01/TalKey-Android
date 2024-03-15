package com.example.talkey_android.data.domain.repository.remote.response.messages

import com.google.gson.annotations.SerializedName

data class ListMessageResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("rows")
    val rows: ArrayList<MessageResponse>?
)
