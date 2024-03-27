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
import com.example.talkey_android.data.domain.use_cases.users.SetOnlineUseCase
import com.example.talkey_android.data.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel(
    private val getListProfilesUseCase: GetListProfilesUseCase,
    private val getListChatsUseCase: GetListChatsUseCase,
    private val getListMessageUseCase: GetListMessageUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val setOnlineUseCase: SetOnlineUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<HomeFragmentUiState>(HomeFragmentUiState.Success(listOf()))
    val uiState: StateFlow<HomeFragmentUiState> = _uiState

    private val chatsList: MutableList<ChatItemListModel> = mutableListOf()
    private var usersList: List<UserItemListModel> = listOf()

    private val _idNewChat = MutableSharedFlow<String>()
    val idNewChat: SharedFlow<String> = _idNewChat
    private val _createNewChatError = MutableSharedFlow<ErrorModel>()
    val createNewChatError: SharedFlow<ErrorModel> = _createNewChatError

    private val _deleteChatSuccess = MutableSharedFlow<Boolean>()
    val deleteChatSuccess: SharedFlow<Boolean> = _deleteChatSuccess
    private val _deleteChatError = MutableSharedFlow<ErrorModel>()
    val deleteChatError: SharedFlow<ErrorModel> = _deleteChatError


    fun doLogout() {
        viewModelScope.launch(Dispatchers.IO) {
            setOnlineUseCase(false)
        }
    }


    fun deleteChat(idChat: String) {
        Log.i(">", "HA CLICADO EN BORRAR CHAT")
        viewModelScope.launch(Dispatchers.IO) {
            when (val baseResponse = deleteChatUseCase(idChat)) {
                is BaseResponse.Success -> {
                    Log.i(">", "EMITE EL SUCCESS DE BORRADO")
                    _deleteChatSuccess.emit(true)
                }

                is BaseResponse.Error -> {
                    Log.i(">", "ERROR BORRANDO CHAT")
                    _deleteChatError.emit(baseResponse.error)
                }
            }
        }
    }


    fun createChat(source: String, target: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val baseResponse = createChatUseCase(source, target)) {
                is BaseResponse.Success -> {
                    _idNewChat.emit(baseResponse.data.chatBasicInfoModel.id)
                }

                is BaseResponse.Error -> {
                    _createNewChatError.emit(baseResponse.error)
                }
            }
        }
    }

    fun getChatsList(idUser: String) {
        if (_uiState.value != HomeFragmentUiState.Loading) {
            chatsList.clear()
            Log.i(">", "Inicio de getChatsList")
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.emit(HomeFragmentUiState.Loading)
                Log.i(">", "ha emitido el cargando")
                val chatsListDeferred = async { getChatsData(idUser) }
                Log.i(">", "Ha rellenado la lista")
                chatsListDeferred.await()
                val msgInfoDeferred = async { getMessagesData() }
                Log.i(">", "Ha puesto los mensajes en la lista")
                msgInfoDeferred.await()
                Log.i(">", "Y ahora emite el SUCCESS")
                _uiState.emit(HomeFragmentUiState.Success(chatsList))
            }
        }
    }

    private suspend fun getMessagesData() {
        for (chat in chatsList) {
            when (val baseResponse = getListMessageUseCase(chat.idChat, 1, 0)) {
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
                    chat.lastMessage = ""
                    chat.dateLastMessage = ""
                }
            }
        }

        chatsList.removeAll { it.dateLastMessage == "" }
        chatsList.sortByDescending { it.dateLastMessage }
        chatsList.sortBy { it.dateLastMessage.length }
    }

    private suspend fun getChatsData(idUser: String) {

        when (val baseResponse = getListChatsUseCase()) {
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

    fun getUsersList() {
        if (_uiState.value != HomeFragmentUiState.Loading) {
            Log.i(">", "Inicio de getChatsList")
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.emit(HomeFragmentUiState.Loading)
                Log.i(">", "Emite loading")
                when (val baseResponse = getListProfilesUseCase()) {
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