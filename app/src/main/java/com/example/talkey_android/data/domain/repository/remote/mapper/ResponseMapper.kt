package com.example.talkey_android.data.domain.repository.remote.mapper

interface ResponseMapper<R, M> {
    fun fromResponse(response: R): M
}