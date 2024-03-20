package com.example.talkey_android.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.messages.ListMessageModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.messages.GetListMessageUseCase
import com.example.talkey_android.data.domain.use_cases.messages.SendMessageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatFragmentViewModel(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getListMessageUseCase: GetListMessageUseCase
) : ViewModel() {
    private val _setMessageError = MutableSharedFlow<ErrorModel>()
    val setMessageError: SharedFlow<ErrorModel> = _setMessageError

    private val _getListMessageError = MutableSharedFlow<ErrorModel>()
    val getListMessageError: SharedFlow<ErrorModel> = _getListMessageError

    private val _message = MutableStateFlow(ListMessageModel())
    val message: StateFlow<ListMessageModel> = _message

    suspend fun sendMessage(token: String, chat: String, source: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val baseResponse = sendMessageUseCase(token, chat, source, message)

            when (baseResponse) {
                is BaseResponse.Success -> {
                    getMessages(token, chat, 200, 0)
                }

                is BaseResponse.Error -> {
                    _setMessageError.emit(baseResponse.error)
                }
            }
        }
    }

    suspend fun getMessages(token: String, idChat: String, limit: Int, offset: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val baseResponse = getListMessageUseCase(token, idChat, limit, offset)

            when (baseResponse) {
                is BaseResponse.Success -> {
                    _message.emit(baseResponse.data)
                }

                is BaseResponse.Error -> {
                    _getListMessageError.emit(baseResponse.error)
                }
            }
        }
    }
}