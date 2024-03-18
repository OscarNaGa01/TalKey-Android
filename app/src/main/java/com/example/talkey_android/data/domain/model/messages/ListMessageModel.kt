package com.example.talkey_android.data.domain.model.messages

data class ListMessageModel(
    val count: Int = 0,
    val rows: ArrayList<MessageModel> = arrayListOf()
)
