package com.example.talkey_android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.chats.ChatItemListModel
import com.example.talkey_android.data.domain.model.chats.ChatModel
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.UserItemListModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.chats.CreateChatUseCase
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.messages.GetListMessageUseCase
import com.example.talkey_android.data.domain.use_cases.users.GetListProfilesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel(
    private val getListProfilesUseCase: GetListProfilesUseCase,
    private val getListChatsUseCase: GetListChatsUseCase,
    private val getListMessageUseCase: GetListMessageUseCase,
    private val createChatUseCase: CreateChatUseCase
) : ViewModel() {

    private val _users = MutableSharedFlow<List<UserItemListModel>>()
    val users: SharedFlow<List<UserItemListModel>> = _users
    private val _getUsersListError = MutableSharedFlow<ErrorModel>()
    val getUsersListError: SharedFlow<ErrorModel> = _getUsersListError

    private val _chats = MutableSharedFlow<List<ChatItemListModel>>()
    val chats: SharedFlow<List<ChatItemListModel>> = _chats
    private val _getChatsListError = MutableSharedFlow<ErrorModel>()
    val getChatsListError: SharedFlow<ErrorModel> = _getChatsListError

    private val chatsList: MutableList<ChatItemListModel> = mutableListOf()

    private val _idNewChat = MutableSharedFlow<String>()
    val idNewChat: SharedFlow<String> = _idNewChat
    private val _createNewChatError = MutableSharedFlow<ErrorModel>()
    val createNewChatError: SharedFlow<ErrorModel> = _createNewChatError

    fun createChat(token: String, source: String, target: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val baseResponse = createChatUseCase(token, source, target)

            when (baseResponse) {
                is BaseResponse.Success -> {
                    _idNewChat.emit(baseResponse.data.chatBasicInfoModel.id)
                }

                is BaseResponse.Error -> {
                    _createNewChatError.emit(baseResponse.error)
                }
            }
        }
    }

    fun getChatsList(token: String, idUser: String) {
        chatsList.clear()
        println("antes de empezar el viewModel")
        viewModelScope.launch(Dispatchers.IO) {
            println("Ha empezado a coger chatsInfo")
            val chatsListDeferred = async { getChatsInfo(token, idUser) }
            chatsListDeferred.await()
            println("Ha terminado de coger chatInfo")

            println("empieza a coger msgInfo")
            val msgInfoDeferred = async { getMessagesInfo(token) }
            println("Terminó de coger msgInfo")
            msgInfoDeferred.await()

            println("Emite el valor de chatList")
            _chats.emit(chatsList)
        }
    }

    private suspend fun getMessagesInfo(token: String) {
        for (chat in chatsList) {
            val baseResponse = getListMessageUseCase(token, chat.idChat, 1, 0)
            when (baseResponse) {
                is BaseResponse.Success -> {
                    println("Ha cogido el caso de uso de ${chat.idChat}")

                    println("Número de elementos en la lista es " + baseResponse.data.rows.count())
                    if (baseResponse.data.count > 0) {
                        chat.lastMessage = baseResponse.data.rows[0].message
                        chat.dateLastMessage = baseResponse.data.rows[0].date.substring(0, 10)
                    } else {
                        chat.lastMessage = "Dile algo a ${chat.contactNick}"
                        chat.dateLastMessage = ""
                    }
                }

                is BaseResponse.Error -> {
                    chat.lastMessage = "No se ha podido cargar el último mensaje"
                    chat.dateLastMessage = "ERROR"
                }
            }
        }
    }

    private suspend fun getChatsInfo(token: String, idUser: String) {
        val baseResponse = getListChatsUseCase(token)

        when (baseResponse) {
            is BaseResponse.Success -> {
                baseResponse.data.chats.forEach { chatModel ->
                    chatsList.add(
                        ChatItemListModel(
                            chatModel.idChat,
                            idUser,
                            selectContactNick(idUser, chatModel),
                            selectContactOnline(idUser, chatModel),
                            selectContactAvatar(idUser, chatModel)
                        )
                    )
                }
            }

            is BaseResponse.Error -> {
                _getChatsListError.emit(baseResponse.error)
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

    fun getUsersList(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val baseResponse = getListProfilesUseCase(token)

            when (baseResponse) {
                is BaseResponse.Success -> {
                    _users.emit(baseResponse.data.users)
                }

                is BaseResponse.Error -> {
                    _getUsersListError.emit(baseResponse.error)
                }
            }
        }
    }

}