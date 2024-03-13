package com.example.talkey_android.data.domain.repository.remote.response.common

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("success")
    val success: Boolean?
)
