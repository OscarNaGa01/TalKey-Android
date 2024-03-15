package com.example.talkey_android.ui.chat

import androidx.lifecycle.ViewModel
import com.example.talkey_android.data.domain.model.common.CommonMessageModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatFragmentViewModel : ViewModel() {
    private val _message = MutableStateFlow<List<CommonMessageModel>>(listOf())
    val message: StateFlow<List<CommonMessageModel>> = _message
}