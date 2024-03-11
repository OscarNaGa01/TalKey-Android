package com.example.talkey_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.UserProfileModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.users.GetProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileFragmentViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _getProfileError = MutableSharedFlow<ErrorModel>()
    val getProfileError: SharedFlow<ErrorModel> = _getProfileError

    private val _userProfile = MutableStateFlow(UserProfileModel())
    val userProfile: StateFlow<UserProfileModel> = _userProfile

    fun getProfile(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val baseResponse = getProfileUseCase(token)
            when (baseResponse) {
                is BaseResponse.Success -> {
                    _userProfile.emit(baseResponse.data)
                }

                is BaseResponse.Error -> {
                    _getProfileError.emit(baseResponse.error)
                }
            }
        }
    }
}