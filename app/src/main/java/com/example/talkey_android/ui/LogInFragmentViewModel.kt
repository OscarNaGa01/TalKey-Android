package com.example.talkey_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.LoginRequestModel
import com.example.talkey_android.data.domain.model.users.RegisterRequestModel
import com.example.talkey_android.data.domain.model.users.UserModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.PostLoginUseCase
import com.example.talkey_android.data.domain.use_cases.PostRegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogInFragmentViewModel(
    private val postRegisterUseCase: PostRegisterUseCase,
    private val postLoginUseCase: PostLoginUseCase
) : ViewModel() {

    private val _registerError = MutableSharedFlow<ErrorModel>()
    val registerError: SharedFlow<ErrorModel> = _registerError

    private val _loginError = MutableSharedFlow<ErrorModel>()
    val loginError: SharedFlow<ErrorModel> = _loginError

    private val _user =
        MutableStateFlow(UserModel())
    val user: StateFlow<UserModel> = _user


    fun postRegister(registerRequestModel: RegisterRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {

            val baseResponse = postRegisterUseCase(registerRequestModel)

            when (baseResponse) {
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
                    _registerError.emit(baseResponse.error)
                }
            }
        }
    }

    fun postLogin(loginRequestModel: LoginRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val baseResponse = postLoginUseCase(loginRequestModel)

            when (baseResponse) {
                is BaseResponse.Success -> {
                    _user.emit(baseResponse.data)
                }

                is BaseResponse.Error -> {
                    _loginError.emit(baseResponse.error)
                }
            }
        }
    }
}