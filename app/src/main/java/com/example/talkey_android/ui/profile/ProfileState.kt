package com.example.talkey_android.ui.profile

sealed class ProfileState {
    data object ShowProfile : ProfileState()
    data object EditProfile : ProfileState()
}