package com.example.talkey_android.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.chats.ChatModel
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.messages.MessageModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
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
    private val getListMessageUseCase: GetListMessageUseCase,
    private val getListChatsUseCase: GetListChatsUseCase
) : ViewModel() {
    private val _setMessageError = MutableSharedFlow<ErrorModel>()
    val setMessageError: SharedFlow<ErrorModel> = _setMessageError

    private val _getListMessageError = MutableSharedFlow<ErrorModel>()
    val getListMessageError: SharedFlow<ErrorModel> = _getListMessageError

    private val _getListChatsError = MutableSharedFlow<ErrorModel>()
    val getListChatsError: SharedFlow<ErrorModel> = _getListChatsError

    private val _message = MutableSharedFlow<List<MessageModel>>()
    val message: SharedFlow<List<MessageModel>> = _message

    private val _contact = MutableStateFlow(ChatModel())
    val contact: StateFlow<ChatModel> = _contact

    private val messageList = mutableListOf<MessageModel>()

    fun sendMessage(token: String, chat: String, source: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {

            when (val baseResponse = sendMessageUseCase(token, chat, source, message)) {
                is BaseResponse.Success -> {
                    getMessages(token, chat, 10, 0)
                }

                is BaseResponse.Error -> {
                    _setMessageError.emit(baseResponse.error)
                }
            }
        }
    }

    fun getMessages(token: String, idChat: String, limit: Int, offset: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            when (val baseResponse = getListMessageUseCase(token, idChat, limit, offset)) {
                is BaseResponse.Success -> {
                    messageList.addAll(baseResponse.data.rows)
                    _message.emit(messageList)
                }

                is BaseResponse.Error -> {
                    _getListMessageError.emit(baseResponse.error)
                }
            }
        }
    }

    fun getContactData(token: String, idChat: String, idUser: String) {
        viewModelScope.launch(Dispatchers.IO) {

            when (val baseResponse = getListChatsUseCase(token)) {
                is BaseResponse.Success -> {
                    baseResponse.data.chats.filter { idChat == it.idChat }.map {
                        _contact.emit(
                            ChatModel(
                                targetNick = selectContactNick(idUser, it),
                                targetOnline = selectContactOnline(idUser, it),
                                targetAvatar = selectContactAvatar(idUser, it)
                            )
                        )
                    }
                }

                is BaseResponse.Error -> {
                    _getListChatsError.emit(baseResponse.error)
                }
            }
        }
    }

    private fun selectContactOnline(idUser: String, chatModel: ChatModel): Boolean {
        return if (idUser == chatModel.source) {
            chatModel.targetOnline
        } else {
            chatModel.sourceOnline
        }
    }

    private fun selectContactNick(idUser: String, chatModel: ChatModel): String {
        return if (idUser == chatModel.source) {
            chatModel.targetNick
        } else {
            chatModel.sourceNick
        }
    }

    private fun selectContactAvatar(idUser: String, chatModel: ChatModel): String {
        return if (idUser == chatModel.source) {
            chatModel.targetAvatar
        } else {
            chatModel.sourceAvatar
        }
    }
}