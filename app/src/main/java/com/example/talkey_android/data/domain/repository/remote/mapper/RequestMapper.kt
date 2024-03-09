package com.example.talkey_android.data.domain.repository.remote.mapper

interface RequestMapper<M, R> {
    fun toRequest(model: M): R
}