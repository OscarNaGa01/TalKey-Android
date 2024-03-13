package com.example.talkey_android.data.domain.model.users

import com.example.talkey_android.data.domain.model.BaseModel

data class ListUsersModel(
    val users: List<UserItemListModel> = listOf()
) : BaseModel()
