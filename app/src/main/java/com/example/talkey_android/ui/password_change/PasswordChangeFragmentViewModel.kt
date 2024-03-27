package com.example.talkey_android.ui.password_change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.common.SuccessModel
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.UpdateProfileModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.users.UpdateProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class PasswordChangeFragmentViewModel(
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _updateProfileError = MutableSharedFlow<ErrorModel>()
    val updateProfileError: SharedFlow<ErrorModel> = _updateProfileError
    private val _updateProfileSuccess = MutableSharedFlow<SuccessModel>()
    val updateProfileSuccess: SharedFlow<SuccessModel> = _updateProfileSuccess


    fun updateProfile(passwd: String, nick: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val baseResponse = updateProfileUseCase(
                UpdateProfileModel(passwd, nick)
            )
            when (baseResponse) {
                is BaseResponse.Success -> {
                    _updateProfileSuccess.emit(baseResponse.data)
                }

                is BaseResponse.Error -> {
                    _updateProfileError.emit(baseResponse.error)
                }
            }
        }
    }
}