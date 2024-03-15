package com.example.talkey_android.ui.profile

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
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
import java.io.IOException

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

    private val _selectedNewAvatar = MutableSharedFlow<Uri?>()
    val selectedNewAvatar: SharedFlow<Uri?> = _selectedNewAvatar

    fun saveData(passwd: String, nick: String, file: File) {
        viewModelScope.launch(Dispatchers.IO)
        {
            val deferred = listOf(
                async { updateProfile(UpdateProfileModel(passwd, nick)) },
                async { uploadImg(file) }
            )
            deferred.awaitAll()
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

    fun cropImage(uri: Uri, cropActivityResultContract: ActivityResultLauncher<Intent>) {
        val cropIntent = Intent("com.android.camera.action.CROP").apply {
            setDataAndType(uri, "image/*")
            putExtra("crop", "true")
            putExtra("aspectX", 1)
            putExtra("aspectY", 1)
            putExtra("return-data", true)
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
            putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        cropActivityResultContract.launch(cropIntent)
    }

    fun handleCropResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            viewModelScope.launch(Dispatchers.IO) {
                val croppedImageUri = result.data?.data
                _selectedNewAvatar.emit(croppedImageUri)
            }
        } else {
            //TODO("Errores crop")
        }
    }

    fun copyImageToUri(sourceUri: Uri, destUri: Uri, contentResolver: ContentResolver) {
        try {
            val inputStream = contentResolver.openInputStream(sourceUri)
            val outputStream = contentResolver.openOutputStream(destUri)
            if (inputStream != null && outputStream != null) {
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}