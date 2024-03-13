package com.example.talkey_android.ui.profile

sealed class ProfileState {
    object ShowProfile : ProfileState()
    object EditProfile : ProfileState()
    object ChangePassword : ProfileState()
}