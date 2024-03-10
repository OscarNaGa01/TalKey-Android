package com.example.talkey_android.data.domain.repository.remote.request.users

import com.google.gson.annotations.SerializedName

data class FirebaseTokenRequest(
    @SerializedName("firebaseToken")
    val firebaseToken: String?
)
