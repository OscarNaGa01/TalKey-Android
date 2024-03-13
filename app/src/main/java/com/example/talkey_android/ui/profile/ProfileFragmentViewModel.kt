package com.example.talkey_android.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.common.MessageModel
import com.example.talkey_android.data.domain.model.common.SuccessModel
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.UpdateProfileModel
import com.example.talkey_android.data.domain.model.users.UserProfileModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.users.GetProfileUseCase
import com.example.talkey_android.data.domain.use_cases.users.SetOnlineUseCase
import com.example.talkey_android.data.domain.use_cases.users.UpdateProfileUseCase
import com.example.talkey_android.data.domain.use_cases.users.UploadImgUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ProfileFragmentViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val setOnlineUseCase: SetOnlineUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val uploadImgUseCase: UploadImgUseCase
) : ViewModel() {

    private val _getProfileError = MutableSharedFlow<ErrorModel>()
    val getProfileError: SharedFlow<ErrorModel> = _getProfileError
    private val _userProfile = MutableStateFlow(UserProfileModel())
    val userProfile: StateFlow<UserProfileModel> = _userProfile

    private val _setOnlineError = MutableSharedFlow<ErrorModel>()
    val setOnlineError: SharedFlow<ErrorModel> = _setOnlineError
    private val _setOnlineMessage = MutableStateFlow(MessageModel())
    val setOnlineMessage: StateFlow<MessageModel> = _setOnlineMessage

    private val _updateProfileError = MutableSharedFlow<ErrorModel>()
    val updateProfileError: SharedFlow<ErrorModel> = _updateProfileError
    private val _updateProfileSuccess = MutableStateFlow(SuccessModel())
    val updateProfileSuccess: StateFlow<SuccessModel> = _updateProfileSuccess

    private val _uploadImgError = MutableSharedFlow<ErrorModel>()
    val uploadImgError: SharedFlow<ErrorModel> = _uploadImgError
    private val _uploadImgMessage = MutableStateFlow(MessageModel())
    val uploadImgMessage: StateFlow<MessageModel> = _uploadImgMessage


    fun saveData(passwd: String, nick: String, isOnline: Boolean, file: File) {
        viewModelScope.launch(Dispatchers.IO)
        {
            val deferreds = listOf(
                async { updateProfile(UpdateProfileModel(passwd, nick)) },
                //async { setOnline(isOnline) },
                async { uploadImg(file) }
            )
            deferreds.awaitAll()
            getProfile(_userProfile.value.token)
        }
    }


    private suspend fun uploadImg(file: File) {
        val baseResponse = uploadImgUseCase(
            _userProfile.value.token,
            file
        )
        when (baseResponse) {
            is BaseResponse.Success -> {
                _uploadImgMessage.emit(baseResponse.data)
            }

            is BaseResponse.Error -> {
                _uploadImgError.emit(baseResponse.error)
            }
        }
    }

    private suspend fun updateProfile(updateProfileModel: UpdateProfileModel) {
        val baseResponse = updateProfileUseCase(
            _userProfile.value.token,
            updateProfileModel
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

    fun setOnline(isOnline: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val deferred = async { statusSetter(isOnline) }
            deferred.await()
            getProfile(_userProfile.value.token)
        }
    }

    private suspend fun statusSetter(isOnline: Boolean) {
        val baseResponse = setOnlineUseCase(
            _userProfile.value.token, isOnline
        )
        when (baseResponse) {
            is BaseResponse.Success -> {
                _setOnlineMessage.emit(baseResponse.data)
            }

            is BaseResponse.Error -> {
                _setOnlineError.emit(baseResponse.error)
            }
        }
    }

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