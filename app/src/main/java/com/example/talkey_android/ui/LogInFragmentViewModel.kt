package com.example.talkey_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.RegisterFromResponseModel
import com.example.talkey_android.data.domain.model.users.RegisterToRequestModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.PostRegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogInFragmentViewModel(
    private val postRegisterUseCase: PostRegisterUseCase
) : ViewModel() {

    private val _registerError = MutableSharedFlow<ErrorModel>()
    val registerError: SharedFlow<ErrorModel> = _registerError

    private val _register =
        MutableStateFlow(RegisterFromResponseModel())
    val register: StateFlow<RegisterFromResponseModel> = _register


    fun postRegister(registerToRequestModel: RegisterToRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {

            val baseResponse = postRegisterUseCase(registerToRequestModel)

            when (baseResponse) {
                is BaseResponse.Success -> {
                    _register.emit(baseResponse.data)
                }

                is BaseResponse.Error -> {
                    _registerError.emit(baseResponse.error)
                }
            }
        }
    }
}