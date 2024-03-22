package com.example.talkey_android.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.chats.ChatItemListModel
import com.example.talkey_android.data.domain.model.chats.ChatModel
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.UserItemListModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.chats.CreateChatUseCase
import com.example.talkey_android.data.domain.use_cases.chats.DeleteChatUseCase
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.messages.GetListMessageUseCase
import com.example.talkey_android.data.domain.use_cases.users.GetListProfilesUseCase
import com.example.talkey_android.data.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel(
    private val getListProfilesUseCase: GetListProfilesUseCase,
    private val getListChatsUseCase: GetListChatsUseCase,
    private val getListMessageUseCase: GetListMessageUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val deleteChatUseCase: DeleteChatUseCase
) : ViewModel() {

    private val _uiState = MutableSharedFlow<HomeFragmentUiState>()
    val uiState: SharedFlow<HomeFragmentUiState> = _uiState

    private val chatsList: MutableList<ChatItemListModel> = mutableListOf()
    private var usersList: List<UserItemListModel> = listOf()

    private val _idNewChat = MutableSharedFlow<Pair<String, String>>()
    val idNewChat: SharedFlow<Pair<String, String>> = _idNewChat
    private val _createNewChatError = MutableSharedFlow<ErrorModel>()
    val createNewChatError: SharedFlow<ErrorModel> = _createNewChatError

    private val _deleteChatSuccess = MutableSharedFlow<String>()
    val deleteChatSuccess: SharedFlow<String> = _deleteChatSuccess
    private val _deleteChatError = MutableSharedFlow<ErrorModel>()
    val deleteChatError: SharedFlow<ErrorModel> = _deleteChatError


    fun deleteChat(token: String, idChat: String) {
        Log.i(">", "HA CLICADO EN BORRAR CHAT")
        viewModelScope.launch(Dispatchers.IO) {
            when (val baseResponse = deleteChatUseCase(token, idChat)) {
                is BaseResponse.Success -> {
                    Log.i(">", "EMITE EL SUCCESS DE BORRADO")
                    _deleteChatSuccess.emit("Chat borrado con éxito!")
                }

                is BaseResponse.Error -> {
                    Log.i(">", "ERROR BORRANDO CHAT")
                    _deleteChatError.emit(baseResponse.error)
                }
            }
        }
    }


    fun createChat(token: String, source: String, target: String, nick: String) {
        viewModelScope.launch(Dispatchers.IO) {

            when (val baseResponse = createChatUseCase(token, source, target)) {
                is BaseResponse.Success -> {
                    _idNewChat.emit(Pair(baseResponse.data.chatBasicInfoModel.id, nick))
                }

                is BaseResponse.Error -> {
                    _createNewChatError.emit(baseResponse.error)
                }
            }
        }
    }

    fun getChatsList(token: String, idUser: String) {
        chatsList.clear()
        Log.i(">", "Inicio de getChatsList")
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(HomeFragmentUiState.Loading)
            Log.i(">", "ha emitido el cargando")
            val chatsListDeferred = async { getChatsData(token, idUser) }
            Log.i(">", "Ha rellenado la lista")
            chatsListDeferred.await()
            val msgInfoDeferred = async { getMessagesData(token) }
            Log.i(">", "Ha puesto los mensajes en la lista")
            msgInfoDeferred.await()
            Log.i(">", "Y ahora emite el SUCCESS")
            _uiState.emit(HomeFragmentUiState.Success(chatsList))
        }
    }

    private suspend fun getMessagesData(token: String) {
        for (chat in chatsList) {
            when (val baseResponse = getListMessageUseCase(token, chat.idChat, 1, 0)) {
                is BaseResponse.Success -> {

                    if (baseResponse.data.count > 0) {
                        chat.lastMessage = baseResponse.data.rows[0].message
                        chat.dateLastMessage =
                            Utils.checkDateAndTime(baseResponse.data.rows[0].date, false)
                    } else {
                        chat.dateLastMessage = ""
                    }
                }

                is BaseResponse.Error -> {
                    chat.lastMessage = "No se ha podido cargar el último mensaje"
                    chat.dateLastMessage = "ERROR"
                }
            }
        }

        chatsList.removeAll { it.dateLastMessage == "" }
        chatsList.sortByDescending { it.dateLastMessage }
        chatsList.sortBy { it.dateLastMessage.length }
    }

    private suspend fun getChatsData(token: String, idUser: String) {

        when (val baseResponse = getListChatsUseCase(token)) {
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
                _uiState.emit(HomeFragmentUiState.Error(baseResponse.error.message))
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
        Log.i(">", "Inicio de getChatsList")
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(HomeFragmentUiState.Loading)
            Log.i(">", "Emite loading")
            when (val baseResponse = getListProfilesUseCase(token)) {
                is BaseResponse.Success -> {
                    Log.i(">", "Rellena la lista de usuarios")
                    usersList = baseResponse.data.users
                    Log.i(">", "Emite lista de contactos")
                    _uiState.emit(HomeFragmentUiState.Success(usersList))
                }
                is BaseResponse.Error -> {
                    _uiState.emit(HomeFragmentUiState.Error(baseResponse.error.message))
                }
            }
        }
    }

    fun filterListByName(name: String, listType: HomeFragment.ListType) {

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(HomeFragmentUiState.Loading)
            when (listType) {
                HomeFragment.ListType.CHATS -> {
                    _uiState.emit(HomeFragmentUiState.Success(
                        chatsList.filter {
                            it.contactNick.lowercase().contains(name.lowercase())
                        }
                    ))
                }

                HomeFragment.ListType.CONTACTS -> {
                    _uiState.emit(HomeFragmentUiState.Success(
                        usersList.filter { it.nick.lowercase().contains(name.lowercase()) }
                    ))
                }
            }

        }
    }

    fun removeFilters(listType: HomeFragment.ListType) {
        viewModelScope.launch(Dispatchers.IO) {
            when (listType) {
                HomeFragment.ListType.CHATS ->
                    _uiState.emit(HomeFragmentUiState.Success(chatsList))

                HomeFragment.ListType.CONTACTS ->
                    _uiState.emit(HomeFragmentUiState.Success(usersList))
            }

        }
    }
}