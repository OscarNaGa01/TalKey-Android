package com.example.talkey_android.ui.chat

import androidx.lifecycle.ViewModel
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.messages.ListMessageModel
import com.example.talkey_android.data.domain.use_cases.messages.GetListMessageUseCase
import com.example.talkey_android.data.domain.use_cases.messages.SendMessageUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

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
}