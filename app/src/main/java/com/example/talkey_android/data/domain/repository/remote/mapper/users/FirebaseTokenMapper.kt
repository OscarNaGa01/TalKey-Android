package com.example.talkey_android.data.domain.repository.remote.mapper.users

import com.example.talkey_android.data.domain.repository.remote.mapper.RequestMapper
import com.example.talkey_android.data.domain.repository.remote.request.users.FirebaseTokenRequest

class FirebaseTokenMapper : RequestMapper<String, FirebaseTokenRequest> {
    override fun toRequest(model: String): FirebaseTokenRequest {
        return FirebaseTokenRequest(model)
    }
}