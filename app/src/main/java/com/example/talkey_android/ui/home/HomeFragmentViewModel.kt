package com.example.talkey_android.ui.home

import android.os.Build
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragmentViewModel(
    private val getListProfilesUseCase: GetListProfilesUseCase,
    private val getListChatsUseCase: GetListChatsUseCase,
    private val getListMessageUseCase: GetListMessageUseCase,
    private val createChatUseCase: CreateChatUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<HomeFragmentUiState>(HomeFragmentUiState.Loading)
    val uiState: StateFlow<HomeFragmentUiState> = _uiState

    private val chatsList: MutableList<ChatItemListModel> = mutableListOf()
    private var usersList: List<UserItemListModel> = listOf()

    private val _idNewChat = MutableSharedFlow<String>()
    val idNewChat: SharedFlow<String> = _idNewChat
    private val _createNewChatError = MutableSharedFlow<ErrorModel>()
    val createNewChatError: SharedFlow<ErrorModel> = _createNewChatError

    fun createChat(token: String, source: String, target: String) {
        viewModelScope.launch(Dispatchers.IO) {

            when (val baseResponse = createChatUseCase(token, source, target)) {
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
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(HomeFragmentUiState.Loading)
            val chatsListDeferred = async { getChatsInfo(token, idUser) }
            chatsListDeferred.await()

            val msgInfoDeferred = async { getMessagesInfo(token) }
            msgInfoDeferred.await()

            _uiState.emit(HomeFragmentUiState.Success(chatsList))
        }
    }

    private suspend fun getMessagesInfo(token: String) {
        chatsList.sortByDescending { it.dateLastMessage }
        for (chat in chatsList) {
            when (val baseResponse = getListMessageUseCase(token, chat.idChat, 1, 0)) {
                is BaseResponse.Success -> {

                    if (baseResponse.data.count > 0) {
                        chat.lastMessage = baseResponse.data.rows[0].message
                        //chat.dateLastMessage = baseResponse.data.rows[0].date.substring(0, 10)
                        chat.dateLastMessage = checkDateAndTime(baseResponse.data.rows[0].date)
                        println(baseResponse.data.rows[0].date)
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
        chatsList.removeAll { it.dateLastMessage == "" }
    }

    // TODO: Mover esta función a la clase Utils cuando la añadan en un merge con develop
    private fun checkDateAndTime(lastMsgDate: String): String {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val dateTime = LocalDateTime.parse(lastMsgDate, formatter)
            val date = dateTime.toLocalDate()
            val currentDate = LocalDate.now()

            if (date == currentDate) {
                lastMsgDate.substring(11, 16)
            } else {
                lastMsgDate.substring(0, 10)
            }
        } else {
            lastMsgDate.substring(0, 10)
        }

    }

    private suspend fun getChatsInfo(token: String, idUser: String) {

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
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(HomeFragmentUiState.Loading)
            when (val baseResponse = getListProfilesUseCase(token)) {
                is BaseResponse.Success -> {
                    usersList = baseResponse.data.users

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