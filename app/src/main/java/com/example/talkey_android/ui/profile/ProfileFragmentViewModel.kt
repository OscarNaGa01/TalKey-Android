package com.example.talkey_android.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.common.CommonMessageModel
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
    private val _getProfile = MutableStateFlow(UserProfileModel())
    val getProfile: StateFlow<UserProfileModel> = _getProfile

    private val _setOnlineError = MutableSharedFlow<ErrorModel>()
    val setOnlineError: SharedFlow<ErrorModel> = _setOnlineError
    private val _setOnlineMessage = MutableStateFlow(CommonMessageModel())

    private val _updateProfileError = MutableSharedFlow<ErrorModel>()
    val updateProfileError: SharedFlow<ErrorModel> = _updateProfileError
    private val _updateProfileSuccess = MutableSharedFlow<SuccessModel>()
    val updateProfileSuccess: SharedFlow<SuccessModel> = _updateProfileSuccess

    private val _uploadImgError = MutableSharedFlow<ErrorModel>()
    val uploadImgError: SharedFlow<ErrorModel> = _uploadImgError
    private val _uploadImgMessage = MutableStateFlow(CommonMessageModel())

    private val _selectedNewAvatar = MutableSharedFlow<Uri?>()
    val selectedNewAvatar: SharedFlow<Uri?> = _selectedNewAvatar

    private val loadMutableStateFlow = MutableSharedFlow<Boolean>()
    val loadStateFlow: SharedFlow<Boolean> = loadMutableStateFlow

    fun saveData(passwd: String, nick: String, file: File?) {
        viewModelScope.launch(Dispatchers.IO)
        {
            val deferred = listOf(
                async { updateProfile(UpdateProfileModel(passwd, nick)) },
                async { uploadImg(file) }
            )
            deferred.awaitAll()
            getProfile()
        }
    }


    private suspend fun uploadImg(file: File?) {
        if (file != null) {
            val baseResponse = uploadImgUseCase(
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
    }

    private suspend fun updateProfile(updateProfileModel: UpdateProfileModel) {
        val baseResponse = updateProfileUseCase(
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
        }
    }

    private suspend fun statusSetter(isOnline: Boolean) {
        val baseResponse = setOnlineUseCase(
            isOnline
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

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val baseResponse = getProfileUseCase()) {
                is BaseResponse.Success -> {
                    _getProfile.emit(baseResponse.data)
                }

                is BaseResponse.Error -> {
                    _getProfileError.emit(baseResponse.error)
                }
            }
        }
    }

    fun changeCurrentAvatar(uri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uri != null) {
                _selectedNewAvatar.emit(uri)
            } else {
                _selectedNewAvatar.emit(null)
            }
        }
    }

    fun setLoad(loading: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (loading) {
                loadMutableStateFlow.emit(true)
            } else {
                loadMutableStateFlow.emit(false)
            }
        }

    }

}