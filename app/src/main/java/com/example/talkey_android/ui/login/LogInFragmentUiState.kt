package com.example.talkey_android.ui.login

import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.UserModel

sealed class LogInFragmentUiState {
    data object Loading : LogInFragmentUiState()
    data object Start : LogInFragmentUiState()
    data class Success(val userModel: UserModel) : LogInFragmentUiState()
    data class LoginError(val errorModel: ErrorModel) : LogInFragmentUiState()
    data class RegisterError(val errorModel: ErrorModel) : LogInFragmentUiState()

}