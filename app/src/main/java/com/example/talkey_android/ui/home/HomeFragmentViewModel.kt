package com.example.talkey_android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talkey_android.data.domain.model.chats.ChatModel
import com.example.talkey_android.data.domain.model.error.ErrorModel
import com.example.talkey_android.data.domain.model.users.UserItemListModel
import com.example.talkey_android.data.domain.repository.remote.response.BaseResponse
import com.example.talkey_android.data.domain.use_cases.chats.GetListChatsUseCase
import com.example.talkey_android.data.domain.use_cases.users.GetListProfilesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel(
    private val getListProfilesUseCase: GetListProfilesUseCase,
    private val getListChatsUseCase: GetListChatsUseCase
) : ViewModel() {

    private val _users = MutableSharedFlow<List<UserItemListModel>>()
    val users: SharedFlow<List<UserItemListModel>> = _users
    private val _getUsersListError = MutableSharedFlow<ErrorModel>()
    val getUsersListError: SharedFlow<ErrorModel> = _getUsersListError

    private val _chats = MutableSharedFlow<List<ChatModel>>()
    val chats: SharedFlow<List<ChatModel>> = _chats
    private val _getChatsListError = MutableSharedFlow<ErrorModel>()
    val getChatsListError: SharedFlow<ErrorModel> = _getChatsListError


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

    fun getChatsList(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val baseResponse = getListChatsUseCase(token)

            when (baseResponse) {
                is BaseResponse.Success -> {
                    _chats.emit(baseResponse.data.chats)
                }

                is BaseResponse.Error -> {
                    _getChatsListError.emit(baseResponse.error)
                }
            }
        }
    }

}