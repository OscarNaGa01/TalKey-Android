package com.example.talkey_android.data.domain.model.users

import com.example.talkey_android.data.domain.model.BaseModel


data class UserItemListModel(
    val id: String = "",
    val nick: String = "",
    val avatar: String = "",
    val online: Boolean = false,
    val created: String = ""
) : BaseModel()
