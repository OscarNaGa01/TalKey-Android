package com.example.talkey_android.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.users.LoginUseCase
import com.example.talkey_android.data.domain.use_cases.users.RegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogInFragmentViewModel(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LogInFragmentUiState>(LogInFragmentUiState.Start)
    val uiState: StateFlow<LogInFragmentUiState> = _uiState


    fun postRegister(registerRequestModel: RegisterRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(LogInFragmentUiState.Loading)
            when (val baseResponse = registerUseCase(registerRequestModel)) {
                is BaseResponse.Success -> {

                    with(registerRequestModel) {
                        postLogin(
                            LoginRequestModel(
                                password,
                                login,
                                platform,
                                firebaseToken
                            )
                        )
                    }
                }

                is BaseResponse.Error -> {
                    _uiState.emit(LogInFragmentUiState.RegisterError(baseResponse.error))
                }
            }
        }
    }

    fun postLogin(loginRequestModel: LoginRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(LogInFragmentUiState.Loading)
            when (val baseResponse = loginUseCase(loginRequestModel)) {
                is BaseResponse.Success -> {
                    _uiState.emit(LogInFragmentUiState.Success(baseResponse.data))
                }

                is BaseResponse.Error -> {
                    _uiState.emit(LogInFragmentUiState.LoginError(baseResponse.error))
                }
            }
        }
    }
}